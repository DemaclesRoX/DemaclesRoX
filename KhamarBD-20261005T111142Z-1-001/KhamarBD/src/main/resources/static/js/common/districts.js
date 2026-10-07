/**
 * KhamarBD - Bangladesh 64 Districts & 8 Divisions Registry
 * Provides 64-district data, modal popups, and select box population.
 */

const BD_DIVISIONS = {
    "Dhaka": ["Dhaka", "Gazipur", "Narayanganj", "Tangail", "Kishoreganj", "Manikganj", "Munshiganj", "Narsingdi", "Faridpur", "Gopalganj", "Madaripur", "Rajbari", "Shariatpur"],
    "Chattogram": ["Chattogram", "Cox's Bazar", "Cumilla", "Feni", "Brahmanbaria", "Noakhali", "Chandpur", "Lakshmipur", "Khagrachhari", "Rangamati", "Bandarban"],
    "Rajshahi": ["Rajshahi", "Bogura", "Pabna", "Sirajganj", "Naogaon", "Natore", "Chapainawabganj", "Joypurhat"],
    "Khulna": ["Khulna", "Jashore", "Satkhira", "Kushtia", "Chuadanga", "Jhenaidah", "Magura", "Meherpur", "Narail", "Bagerhat"],
    "Barishal": ["Barishal", "Bhola", "Jhalokati", "Pirojpur", "Barguna", "Patuakhali"],
    "Sylhet": ["Sylhet", "Moulvibazar", "Habiganj", "Sunamganj"],
    "Rangpur": ["Rangpur", "Dinajpur", "Gaibandha", "Kurigram", "Lalmonirhat", "Nilphamari", "Panchagarh", "Thakurgaon"],
    "Mymensingh": ["Mymensingh", "Jamalpur", "Netrokona", "Sherpur"]
};

// Flattened list of all 64 districts with division mapping
const ALL_64_DISTRICTS = [];
Object.entries(BD_DIVISIONS).forEach(([division, districts]) => {
    districts.forEach(district => {
        ALL_64_DISTRICTS.push({ district, division });
    });
});

// Helper: Find division by district name
function getDivisionByDistrict(districtName) {
    if (!districtName) return 'Dhaka';
    const clean = districtName.trim().toLowerCase();
    for (const [div, dists] of Object.entries(BD_DIVISIONS)) {
        if (dists.some(d => d.toLowerCase() === clean)) {
            return div;
        }
    }
    return 'Dhaka';
}

// Helper: Populate a <select> element with all 64 districts grouped by division
function populateDistrictSelect(selectElementId, selectedValue = '', includeAllOption = false, allOptionLabel = 'All 64 Districts (All Bangladesh)') {
    const select = typeof selectElementId === 'string' ? document.getElementById(selectElementId) : selectElementId;
    if (!select) return;

    select.innerHTML = '';
    if (includeAllOption) {
        const defaultOpt = document.createElement('option');
        defaultOpt.value = '';
        defaultOpt.textContent = allOptionLabel;
        select.appendChild(defaultOpt);
    }

    Object.entries(BD_DIVISIONS).forEach(([division, districts]) => {
        const optgroup = document.createElement('optgroup');
        optgroup.label = `${division} Division (${districts.length})`;
        districts.forEach(district => {
            const opt = document.createElement('option');
            opt.value = district;
            opt.textContent = district;
            if (selectedValue && district.toLowerCase() === selectedValue.toLowerCase()) {
                opt.selected = true;
            }
            optgroup.appendChild(opt);
        });
        select.appendChild(optgroup);
    });
}

// Modal Popup Controller for 64 District Selection
let _onDistrictSelectedCallback = null;

function openDistrictPickerModal(currentSelected = '', onSelectCallback) {
    _onDistrictSelectedCallback = onSelectCallback;
    let modal = document.getElementById('districtPickerModal');

    if (!modal) {
        modal = document.createElement('div');
        modal.id = 'districtPickerModal';
        modal.className = 'modal';
        modal.innerHTML = `
            <div class="modal-content" style="max-width: 680px; max-height: 85vh; display: flex; flex-direction: column; padding: 1.5rem;">
                <div class="modal-header" style="padding-bottom: 0.75rem; border-bottom: 1px solid var(--border-color);">
                    <div>
                        <span class="badge badge-primary" style="margin-bottom: 4px;">Location Coverage</span>
                        <h3 style="margin: 0; font-size: 1.25rem; font-weight: 800; color: var(--text-main);">
                            <i class="ph-fill ph-map-pin" style="color: var(--primary-color);"></i> Select Your District (64 Districts)
                        </h3>
                    </div>
                    <button type="button" class="close-btn" onclick="closeDistrictPickerModal()">&times;</button>
                </div>

                <!-- Search and Division Filter Tabs -->
                <div style="margin-top: 1rem;">
                    <div style="position: relative; margin-bottom: 0.75rem;">
                        <input type="text" id="districtModalSearch" class="form-control" placeholder="Type district name (e.g. Bogura, Sylhet, Cumilla, Rajshahi)..." style="padding-left: 2.25rem;" oninput="filterDistrictGrid()">
                        <i class="ph ph-magnifying-glass" style="position: absolute; left: 10px; top: 50%; transform: translateY(-50%); color: var(--text-muted); font-size: 1.1rem;"></i>
                    </div>

                    <!-- Division Chips Filter -->
                    <div id="divisionChipsRow" style="display: flex; gap: 6px; overflow-x: auto; padding-bottom: 6px; scrollbar-width: thin;">
                        <button type="button" class="btn btn-xs btn-primary division-chip active" onclick="selectDivisionFilter('ALL', this)">All (64)</button>
                        ${Object.keys(BD_DIVISIONS).map(div => `
                            <button type="button" class="btn btn-xs btn-outline division-chip" onclick="selectDivisionFilter('${div}', this)">${div}</button>
                        `).join('')}
                    </div>
                </div>

                <!-- 64 District Buttons Grid -->
                <div id="districtGridContainer" style="overflow-y: auto; margin-top: 1rem; flex: 1; display: grid; grid-template-columns: repeat(auto-fill, minmax(140px, 1fr)); gap: 8px; max-height: 380px; padding-right: 4px;">
                    <!-- Dynamically populated -->
                </div>

                <div style="margin-top: 1rem; padding-top: 0.75rem; border-top: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center; font-size: 0.8rem; color: var(--text-muted);">
                    <span><i class="ph ph-globe"></i> Connected across all 8 divisions of Bangladesh</span>
                    <button type="button" class="btn btn-outline btn-sm" onclick="closeDistrictPickerModal()">Cancel</button>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
    }

    renderDistrictGrid('ALL', '', currentSelected);
    modal.classList.add('active');
    setTimeout(() => {
        const searchInput = document.getElementById('districtModalSearch');
        if (searchInput) searchInput.focus();
    }, 150);
}

function closeDistrictPickerModal() {
    const modal = document.getElementById('districtPickerModal');
    if (modal) modal.classList.remove('active');
}

let _currentDivisionFilter = 'ALL';

function selectDivisionFilter(division, btnEl) {
    _currentDivisionFilter = division;
    document.querySelectorAll('.division-chip').forEach(b => b.classList.remove('btn-primary', 'active'));
    document.querySelectorAll('.division-chip').forEach(b => b.classList.add('btn-outline'));
    if (btnEl) {
        btnEl.classList.remove('btn-outline');
        btnEl.classList.add('btn-primary', 'active');
    }
    const searchVal = document.getElementById('districtModalSearch') ? document.getElementById('districtModalSearch').value : '';
    renderDistrictGrid(_currentDivisionFilter, searchVal);
}

function filterDistrictGrid() {
    const searchVal = document.getElementById('districtModalSearch') ? document.getElementById('districtModalSearch').value : '';
    renderDistrictGrid(_currentDivisionFilter, searchVal);
}

function renderDistrictGrid(divisionFilter = 'ALL', search = '', currentSelected = '') {
    const container = document.getElementById('districtGridContainer');
    if (!container) return;

    const cleanSearch = search.toLowerCase().trim();
    let filtered = ALL_64_DISTRICTS.filter(item => {
        const matchesDiv = (divisionFilter === 'ALL' || item.division.toUpperCase() === divisionFilter.toUpperCase());
        const matchesSearch = !cleanSearch || item.district.toLowerCase().includes(cleanSearch) || item.division.toLowerCase().includes(cleanSearch);
        return matchesDiv && matchesSearch;
    });

    if (filtered.length === 0) {
        container.innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; padding: 2rem; color: var(--text-muted);">
                <i class="ph ph-map-pin" style="font-size: 2rem; display: block; margin-bottom: 0.5rem; opacity: 0.5;"></i>
                No district found matching "${search}".
            </div>
        `;
        return;
    }

    container.innerHTML = filtered.map(item => {
        const isSelected = currentSelected && item.district.toLowerCase() === currentSelected.toLowerCase();
        return `
            <button type="button" class="district-select-btn" onclick="handleDistrictChosen('${item.district}', '${item.division}')" style="
                background: ${isSelected ? 'var(--primary-ultra-light)' : 'var(--surface-color)'};
                border: 1px solid ${isSelected ? 'var(--primary-color)' : 'var(--border-color)'};
                border-radius: var(--radius-sm);
                padding: 0.65rem 0.75rem;
                text-align: left;
                cursor: pointer;
                display: flex;
                flex-direction: column;
                gap: 2px;
                transition: all 0.15s ease;
            " onmouseover="this.style.borderColor='var(--primary-color)'; this.style.transform='translateY(-1px)'" onmouseout="if(!${isSelected}) { this.style.borderColor='var(--border-color)'; this.style.transform='none'; }">
                <span style="font-weight: 700; font-size: 0.875rem; color: var(--text-main);">${item.district}</span>
                <span style="font-size: 0.7rem; color: var(--text-muted);">${item.division} Div.</span>
            </button>
        `;
    }).join('');
}

function handleDistrictChosen(district, division) {
    if (typeof _onDistrictSelectedCallback === 'function') {
        _onDistrictSelectedCallback(district, division);
    }
    closeDistrictPickerModal();
}
