// KhamarBD - Universal Multi-Role Real-Time Chat Engine
// Supports: Supplier <-> Farmer, Buyer <-> Farmer, Specialist <-> Farmer, and Peer-to-Peer
(function () {
    const POLL_MS = 3500;

    let me = null;
    let threads = [];          // [{ consultation, other, last }]
    let activeId = null;       // active consultationId
    let lastMsgId = 0;
    let pollTimer = null;
    const profileCache = {};

    function resolveUser() {
        const u = (typeof getCurrentUser === 'function') ? getCurrentUser() : null;
        if (u && u.userId) return u;
        const pageRole = window.CHAT_ROLE || 'FARMER';
        if (pageRole === 'SUPPLIER') return { userId: 3, name: 'Supplier', primaryRole: 'SUPPLIER' };
        if (pageRole === 'BUYER') return { userId: 5, name: 'Commercial Buyer', primaryRole: 'BUYER' };
        if (pageRole === 'SPECIALIST') return { userId: 2, name: 'Dr. Specialist', primaryRole: 'SPECIALIST' };
        return { userId: 1, name: 'Farmer Rahim', primaryRole: 'FARMER' };
    }

    function esc(s) {
        return String(s == null ? '' : s)
            .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;').replace(/'/g, '&#39;');
    }

    function fmtTime(iso) {
        if (!iso) return '';
        const d = new Date(iso);
        const today = new Date();
        if (d.toDateString() === today.toDateString()) {
            return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        }
        return d.toLocaleDateString([], { day: 'numeric', month: 'short' });
    }

    function normalizeRole(role) {
        const r = (role || '').toUpperCase();
        if (r.includes('SUPPLIER')) return 'SUPPLIER';
        if (r.includes('BUYER')) return 'BUYER';
        if (r.includes('SPEC') || r.includes('VET') || r.includes('DOCTOR')) return 'SPECIALIST';
        if (r.includes('ADMIN')) return 'ADMIN';
        return 'FARMER';
    }

    function getRoleMeta(role) {
        const nr = normalizeRole(role);
        switch (nr) {
            case 'SUPPLIER':
                return {
                    label: 'Input Supplier',
                    icon: 'ph-storefront',
                    bgClass: 'bg-supplier',
                    avatarStyle: 'background: rgba(59, 130, 246, 0.15); color: #2563eb;',
                    badgeStyle: 'background: rgba(59, 130, 246, 0.12); color: #1d4ed8; border: 1px solid rgba(59, 130, 246, 0.25);'
                };
            case 'BUYER':
                return {
                    label: 'Commercial Buyer',
                    icon: 'ph-shopping-bag-open',
                    bgClass: 'bg-buyer',
                    avatarStyle: 'background: rgba(245, 158, 11, 0.15); color: #d97706;',
                    badgeStyle: 'background: rgba(245, 158, 11, 0.12); color: #b45309; border: 1px solid rgba(245, 158, 11, 0.25);'
                };
            case 'SPECIALIST':
                return {
                    label: 'Veterinary Specialist',
                    icon: 'ph-stethoscope',
                    bgClass: 'bg-specialist',
                    avatarStyle: 'background: rgba(16, 185, 129, 0.15); color: #059669;',
                    badgeStyle: 'background: rgba(16, 185, 129, 0.12); color: #047857; border: 1px solid rgba(16, 185, 129, 0.25);'
                };
            case 'ADMIN':
                return {
                    label: 'System Admin',
                    icon: 'ph-shield-check',
                    bgClass: 'bg-admin',
                    avatarStyle: 'background: rgba(239, 68, 68, 0.15); color: #dc2626;',
                    badgeStyle: 'background: rgba(239, 68, 68, 0.12); color: #b91c1c; border: 1px solid rgba(239, 68, 68, 0.25);'
                };
            default:
                return {
                    label: 'Farmer',
                    icon: 'ph-plant',
                    bgClass: 'bg-farmer',
                    avatarStyle: 'background: rgba(34, 197, 94, 0.15); color: #16a34a;',
                    badgeStyle: 'background: rgba(34, 197, 94, 0.12); color: #15803d; border: 1px solid rgba(34, 197, 94, 0.25);'
                };
        }
    }

    async function getProfile(id) {
        if (!id) return null;
        if (profileCache[id]) return profileCache[id];
        try {
            const p = await apiRequest('/user/filter/' + id + '/profile');
            profileCache[id] = p;
            return p;
        } catch (e) {
            return null;
        }
    }

    function avatarHtml(role, isHeader) {
        const meta = getRoleMeta(role);
        const style = isHeader
            ? `${meta.avatarStyle} width: 44px; height: 44px; font-size: 1.35rem; display: flex; align-items: center; justify-content: center; border-radius: 50%;`
            : `${meta.avatarStyle} width: 40px; height: 40px; font-size: 1.2rem; display: flex; align-items: center; justify-content: center; border-radius: 50%;`;
        return `<div class="chat-avatar" style="${style}"><i class="ph-fill ${meta.icon}"></i></div>`;
    }

    function getOtherInfo(c, profile) {
        const isFarmer = (c.farmerId === me.userId);
        const otherId = isFarmer ? c.specialistId : c.farmerId;
        const rawRole = (profile && (profile.primaryRole || profile.role))
            || (isFarmer ? c.specialistRole : c.farmerRole);
        const name = (profile && profile.name)
            || (isFarmer ? c.specialistName : c.farmerName)
            || `User #${otherId}`;
        const phone = (profile && profile.phone)
            || (isFarmer ? c.specialistPhone : c.farmerPhone);
        const location = (profile && [profile.upazila, profile.district].filter(Boolean).join(', '))
            || (isFarmer ? c.specialistLocation : c.farmerLocation)
            || '';
        return {
            id: otherId,
            name: name,
            role: rawRole,
            phone: phone,
            location: location
        };
    }

    async function loadThreads() {
        const listEl = document.getElementById('chatList');
        if (!listEl) return;
        listEl.innerHTML = '<p class="text-muted" style="padding: 1rem; text-align: center;"><i class="ph ph-spinner ph-spin"></i> Loading conversations...</p>';

        let list = [];
        try {
            list = await apiRequest('/consultation/search?userId=' + me.userId);
        } catch (e) {
            // Fallback to role-specific query if needed
            try {
                const q = me.primaryRole === 'SPECIALIST' ? ('specialistId=' + me.userId) : ('farmerId=' + me.userId);
                list = await apiRequest('/consultation/search?' + q);
            } catch (err) {
                listEl.innerHTML = '<p class="text-danger" style="padding: 1rem; text-align: center;">Failed to load conversations.</p>';
                showEmptyMain('Could not connect to the server.');
                return;
            }
        }

        // Active non-rejected conversations
        const open = (Array.isArray(list) ? list : []).filter(c => c.status !== 'CANCELLED' && c.status !== 'REJECTED');

        threads = await Promise.all(open.map(async c => {
            const isFarmer = (c.farmerId === me.userId);
            const otherId = isFarmer ? c.specialistId : c.farmerId;
            const otherProfile = await getProfile(otherId);
            const other = getOtherInfo(c, otherProfile);

            let last = null;
            try {
                const msgs = await apiRequest('/chat/consultation/' + c.consultationId);
                if (Array.isArray(msgs) && msgs.length) last = msgs[msgs.length - 1];
            } catch (e) { /* ignore preview errors */ }
            return { consultation: c, other: other, last: last };
        }));

        // Sort by most recent activity
        threads.sort((a, b) => {
            const ta = new Date(a.last ? a.last.sentAt : (a.consultation.updatedAt || a.consultation.createdAt || 0));
            const tb = new Date(b.last ? b.last.sentAt : (b.consultation.updatedAt || b.consultation.createdAt || 0));
            return tb - ta;
        });

        // Check URL parameters for direct chat initiation (?peerId=... or ?consultationId=...)
        const params = new URLSearchParams(window.location.search);
        const targetPeerId = parseInt(params.get('peerId'), 10);
        const wantedConsultationId = parseInt(params.get('consultationId'), 10);

        if (targetPeerId && targetPeerId !== me.userId) {
            await handleDirectPeerNavigation(targetPeerId, params);
            return;
        }

        if (threads.length === 0) {
            listEl.innerHTML = '<p class="text-muted" style="padding: 1.5rem; text-align: center;">No active conversations yet.</p>';
            showEmptyMain('Start a conversation from the Marketplace, Orders, or Consultations page.');
            return;
        }

        renderThreadList();

        const exists = threads.some(t => t.consultation.consultationId === wantedConsultationId);
        openThread(exists ? wantedConsultationId : threads[0].consultation.consultationId);
    }

    async function handleDirectPeerNavigation(peerId, params) {
        // Check if thread already exists with this peer
        let existing = threads.find(t => t.other.id === peerId);
        if (existing) {
            renderThreadList();
            openThread(existing.consultation.consultationId);
            return;
        }

        // Auto-create direct conversation thread
        const topic = params.get('topic') || params.get('name') || 'Direct Conversation';
        try {
            const created = await apiRequest('/consultation/direct-thread', 'POST', {
                user1Id: me.userId,
                user2Id: peerId,
                topic: topic
            });

            const otherProfile = await getProfile(peerId);
            const other = getOtherInfo(created, otherProfile);
            const newThread = { consultation: created, other: other, last: null };
            threads.unshift(newThread);
            renderThreadList();
            openThread(created.consultationId);
        } catch (e) {
            console.error('Failed to create direct thread', e);
            renderThreadList();
            if (threads.length > 0) openThread(threads[0].consultation.consultationId);
        }
    }

    function renderThreadList() {
        const listEl = document.getElementById('chatList');
        if (!listEl) return;
        const q = (document.getElementById('chatSearch').value || '').toLowerCase().trim();
        const visible = threads.filter(t => {
            if (!q) return true;
            return t.other.name.toLowerCase().includes(q)
                || (t.other.role && t.other.role.toLowerCase().includes(q))
                || (t.other.location && t.other.location.toLowerCase().includes(q))
                || ('#' + t.consultation.consultationId).includes(q);
        });

        if (visible.length === 0) {
            listEl.innerHTML = '<p class="text-muted" style="padding: 1.5rem; text-align: center;">No matching conversations.</p>';
            return;
        }

        listEl.innerHTML = visible.map(t => {
            const c = t.consultation;
            const meta = getRoleMeta(t.other.role);
            const preview = t.last
                ? ((t.last.senderId === me.userId ? 'You: ' : '') + t.last.content)
                : (c.problemDescription || 'Click to start conversation');
            const time = t.last ? fmtTime(t.last.sentAt) : fmtTime(c.updatedAt || c.createdAt);

            return `<div class="chat-item${c.consultationId === activeId ? ' active' : ''}" data-id="${c.consultationId}">
                ${avatarHtml(t.other.role, false)}
                <div class="chat-preview" style="flex: 1; min-width: 0;">
                    <div style="display: flex; align-items: center; justify-content: space-between; gap: 0.5rem; margin-bottom: 0.15rem;">
                        <h4 style="margin: 0; font-size: 0.95rem; font-weight: 700; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">${esc(t.other.name)}</h4>
                        <span class="chat-time" style="font-size: 0.75rem; color: var(--text-muted); flex-shrink: 0;">${esc(time)}</span>
                    </div>
                    <div style="display: flex; align-items: center; gap: 0.4rem; margin-bottom: 0.25rem;">
                        <span class="badge" style="${meta.badgeStyle} font-size: 0.65rem; padding: 0.1rem 0.45rem; border-radius: 9999px;">
                            <i class="ph-fill ${meta.icon}"></i> ${esc(meta.label)}
                        </span>
                        ${t.other.location ? `<span style="font-size: 0.75rem; color: var(--text-muted); white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"><i class="ph ph-map-pin"></i> ${esc(t.other.location)}</span>` : ''}
                    </div>
                    <p style="margin: 0; font-size: 0.82rem; color: var(--text-muted); white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">${esc(preview)}</p>
                </div>
            </div>`;
        }).join('');

        listEl.querySelectorAll('.chat-item').forEach(el => {
            el.addEventListener('click', () => openThread(parseInt(el.dataset.id, 10)));
        });
    }

    function showEmptyMain(text) {
        document.getElementById('chatHeader').style.display = 'none';
        document.getElementById('chatContext').style.display = 'none';
        document.getElementById('chatForm').style.display = 'none';
        document.getElementById('messageContainer').innerHTML =
            `<div style="margin: auto; text-align: center; color: var(--text-muted); padding: 3rem 1.5rem;">
                <div style="width: 72px; height: 72px; border-radius: 50%; background: var(--surface-hover, #f1f5f9); display: flex; align-items: center; justify-content: center; margin: 0 auto 1.25rem;">
                    <i class="ph ph-chats-circle" style="font-size: 2.75rem; color: var(--primary, #16a34a);"></i>
                </div>
                <h3 style="margin-bottom: 0.5rem; color: var(--text-main, #1e293b);">Direct Real-Time Messaging</h3>
                <p style="max-width: 440px; margin: 0 auto; line-height: 1.5;">${esc(text)}</p>
            </div>`;
    }

    async function openThread(consultationId) {
        const t = threads.find(x => x.consultation.consultationId === consultationId);
        if (!t) return;
        activeId = consultationId;
        lastMsgId = 0;
        renderThreadList();

        const c = t.consultation;
        const other = t.other;
        const meta = getRoleMeta(other.role);

        document.getElementById('chatHeader').style.display = 'flex';
        document.getElementById('chatContext').style.display = 'flex';
        document.getElementById('chatForm').style.display = 'flex';

        document.getElementById('chatHeaderAvatar').innerHTML = avatarHtml(other.role, true);
        document.getElementById('chatRecipientName').textContent = other.name;

        const roleBadgeHtml = `<span class="badge" style="${meta.badgeStyle} font-size: 0.72rem; padding: 0.15rem 0.5rem; border-radius: 9999px; margin-right: 0.4rem;">
            <i class="ph-fill ${meta.icon}"></i> ${esc(meta.label)}
        </span>`;
        const locationText = other.location ? ` • <i class="ph ph-map-pin"></i> ${esc(other.location)}` : '';
        document.getElementById('chatRecipientMeta').innerHTML = roleBadgeHtml + locationText;

        const callBtn = document.getElementById('chatCallBtn');
        if (callBtn) {
            if (other.phone) {
                callBtn.href = 'tel:' + other.phone;
                callBtn.style.display = 'inline-flex';
                callBtn.title = 'Call ' + other.phone;
            } else {
                callBtn.style.display = 'none';
            }
        }

        // Context Banner Configuration
        const badgeEl = document.getElementById('chatContextBadge');
        const descEl = document.getElementById('chatContextDesc');
        const actionBtn = document.getElementById('chatContextActionBtn');

        const normOtherRole = normalizeRole(other.role);
        if (normOtherRole === 'SUPPLIER') {
            badgeEl.textContent = 'Input Procurement & Supply';
            badgeEl.className = 'badge badge-primary';
            descEl.textContent = c.problemDescription ? `Topic: ${c.problemDescription}` : 'Feed, Medicine & Equipment Inquiries & Dispatch';
            if (actionBtn) {
                actionBtn.textContent = 'Browse Marketplace';
                actionBtn.href = '/pages/public/marketplace.html';
                actionBtn.style.display = 'inline-block';
            }
        } else if (normOtherRole === 'BUYER') {
            badgeEl.textContent = 'Commercial Produce Purchase';
            badgeEl.className = 'badge badge-warning';
            descEl.textContent = c.problemDescription ? `Topic: ${c.problemDescription}` : 'Bulk Wholesale, Restaurant & Hall Dining Negotiation';
            if (actionBtn) {
                actionBtn.textContent = 'View Orders';
                actionBtn.href = me.primaryRole === 'BUYER' ? '/pages/buyer/orders.html' : '/pages/farmer/orders.html';
                actionBtn.style.display = 'inline-block';
            }
        } else if (normOtherRole === 'SPECIALIST') {
            badgeEl.textContent = `Veterinary Consultation #${c.consultationId}`;
            badgeEl.className = 'badge badge-success';
            const apptText = c.appointmentDate ? new Date(c.appointmentDate).toLocaleString([], { dateStyle: 'medium', timeStyle: 'short' }) : 'Scheduled';
            descEl.textContent = `Status: ${c.status} • Appointment: ${apptText}`;
            if (actionBtn) {
                actionBtn.textContent = 'View Consultations';
                actionBtn.href = me.primaryRole === 'SPECIALIST' ? '/pages/specialist/consultations.html' : '/pages/farmer/consultations.html';
                actionBtn.style.display = 'inline-block';
            }
        } else {
            badgeEl.textContent = `Direct Conversation #${c.consultationId}`;
            badgeEl.className = 'badge badge-info';
            descEl.textContent = c.problemDescription ? `Topic: ${c.problemDescription}` : 'Direct Farmer-to-Farmer / Multi-Role Collaboration';
            if (actionBtn) {
                actionBtn.style.display = 'none';
            }
        }

        document.getElementById('messageContainer').innerHTML =
            '<p class="text-muted" style="margin: auto; text-align: center;"><i class="ph ph-spinner ph-spin"></i> Loading messages...</p>';

        await fetchMessages(true);
        restartPolling();
        const input = document.getElementById('chatInput');
        if (input) input.focus();
    }

    function messageHtml(m) {
        const mine = m.senderId === me.userId;
        return `<div class="message ${mine ? 'msg-sent' : 'msg-received'}" data-mid="${m.messageId}">
            <div class="msg-content">${esc(m.content).replace(/\n/g, '<br>')}</div>
            <span class="msg-time" style="display: block; font-size: 0.7rem; margin-top: 0.25rem; opacity: 0.75; text-align: ${mine ? 'right' : 'left'};">${esc(fmtTime(m.sentAt))}</span>
        </div>`;
    }

    async function fetchMessages(initial) {
        if (!activeId) return;
        const requestedFor = activeId;
        let msgs = [];
        try {
            const url = '/chat/consultation/' + activeId + (initial ? '' : ('?afterId=' + lastMsgId));
            msgs = await apiRequest(url);
        } catch (e) {
            if (initial) {
                document.getElementById('messageContainer').innerHTML =
                    '<p class="text-danger" style="margin: auto; text-align: center;">Failed to load messages.</p>';
            }
            return;
        }
        if (requestedFor !== activeId) return; // user switched thread

        const box = document.getElementById('messageContainer');
        if (initial) {
            box.innerHTML = (Array.isArray(msgs) && msgs.length)
                ? msgs.map(messageHtml).join('')
                : `<div class="chat-empty-hint" style="margin: auto; text-align: center; color: var(--text-muted); padding: 2rem;">
                    <i class="ph ph-chat-text" style="font-size: 2.2rem; color: var(--primary, #16a34a); opacity: 0.8;"></i>
                    <p style="margin-top: 0.5rem; font-size: 0.95rem;">No messages yet in this conversation.</p>
                    <p style="font-size: 0.82rem; opacity: 0.8;">Say hello and discuss produce, inputs, or farm health!</p>
                   </div>`;
        } else if (Array.isArray(msgs) && msgs.length) {
            const hint = box.querySelector('.chat-empty-hint');
            if (hint) hint.remove();
            msgs.forEach(m => {
                if (!box.querySelector(`[data-mid="${m.messageId}"]`)) {
                    box.insertAdjacentHTML('beforeend', messageHtml(m));
                }
            });
        }

        if (Array.isArray(msgs) && msgs.length) {
            lastMsgId = Math.max(lastMsgId, msgs[msgs.length - 1].messageId);
            const t = threads.find(x => x.consultation.consultationId === activeId);
            if (t) {
                t.last = msgs[msgs.length - 1];
                renderThreadList();
            }
            box.scrollTop = box.scrollHeight;
        }
    }

    function restartPolling() {
        if (pollTimer) clearInterval(pollTimer);
        pollTimer = setInterval(() => fetchMessages(false), POLL_MS);
    }

    async function sendMessage() {
        const input = document.getElementById('chatInput');
        const text = input.value.trim();
        if (!text || !activeId) return;

        const btn = document.querySelector('#chatForm .btn-send');
        if (btn) btn.disabled = true;
        try {
            await apiRequest('/chat/send', 'POST', {
                consultationId: activeId,
                senderId: me.userId,
                content: text
            });
            input.value = '';
            await fetchMessages(false);
        } catch (e) {
            alert('Message delivery error: ' + e.message);
        } finally {
            if (btn) btn.disabled = false;
            input.focus();
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        me = resolveUser();
        const chatForm = document.getElementById('chatForm');
        if (chatForm) chatForm.addEventListener('submit', e => { e.preventDefault(); sendMessage(); });
        const searchInput = document.getElementById('chatSearch');
        if (searchInput) searchInput.addEventListener('input', renderThreadList);
        loadThreads();
    });
})();