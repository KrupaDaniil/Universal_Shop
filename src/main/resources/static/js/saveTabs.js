document.addEventListener('DOMContentLoaded', function () {

    document.querySelectorAll('button[data-bs-toggle="tab"]').forEach(function(tabButton) {
        tabButton.addEventListener('shown.bs.tab', function (e) {
            let activeTab = e.target.id; // Отримуємо ID кнопки

            localStorage.setItem('activeTab', activeTab); // Зберігаємо ID
        });
    });

    // Відновлюємо активну вкладку при завантаженні сторінки
    let activeTab = localStorage.getItem('activeTab');

    if (activeTab) {
        let tabToActivate = document.querySelector('#' + activeTab);
        if (tabToActivate) {
            let tab = new bootstrap.Tab(tabToActivate);
            tab.show(); // Показуємо збережену вкладку
        } else {
            let firstTabButton = document.querySelector('button[data-bs-toggle="tab"]');
            if (firstTabButton) {
                let tab = new bootstrap.Tab(firstTabButton);
                tab.show();
            }
        }
    } else {
        // Якщо activeTab немає, активуємо першу вкладку за замовчуванням
        let firstTabButton = document.querySelector('button[data-bs-toggle="tab"]');
        if (firstTabButton) {
            let tab = new bootstrap.Tab(firstTabButton);
            tab.show();
        }
    }
});