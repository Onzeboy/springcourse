let currentBoxCount = 6;
const boxesContainer = document.getElementById('boxes-container');
const loadMoreBtn = document.getElementById('load-more-btn');

function loadMore() {
    const itemsToAdd = 6;
    for (let i = 0; i < itemsToAdd; i++) {
        currentBoxCount++;
        const newBox = document.createElement('div');
        newBox.className = 'box';
        newBox.textContent = `Box ${currentBoxCount}`;
        boxesContainer.appendChild(newBox);
    }
}

// Инициализация первой страницы
window.onload = loadMore;
