/**
 * KhamarBD - Master Application Client Logic
 */

document.addEventListener('DOMContentLoaded', () => {
    console.log("KhamarBD Frontend Initialized.");

    // Handle Farm Selector change if on dashboard
    const farmSelect = document.getElementById('farmSelect');
    if (farmSelect) {
        farmSelect.addEventListener('change', (e) => {
            const selectedText = e.target.options[e.target.selectedIndex].text;
            console.log(`Active Farm switched to: ${selectedText}`);
        });
    }

    // Smooth scroll for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const targetId = this.getAttribute('href');
            if (targetId && targetId !== '#') {
                const targetElement = document.querySelector(targetId);
                if (targetElement) {
                    e.preventDefault();
                    targetElement.scrollIntoView({ behavior: 'smooth' });
                }
            }
        });
    });
});
