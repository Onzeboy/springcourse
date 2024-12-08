document.addEventListener('DOMContentLoaded', function() {
    const loadMoreButton = document.getElementById('load-more');
    const container = document.getElementById('product-container');
    const initialProducts = container.querySelectorAll('.product-box').length;

    if (initialProducts < 12) {
        loadMoreButton.style.display = 'none';
    }

    loadMoreButton.addEventListener('click', function() {
        let page = parseInt(loadMoreButton.getAttribute('data-page'));
        let size = parseInt(loadMoreButton.getAttribute('data-size'));

        fetch(`/products/loadMore?page=${page}&size=${size}`)
            .then(response => response.json())
            .then(data => {
                if (data.content.length === 0 || data.content.length < size) {
                    loadMoreButton.style.display = 'none'; // Скрываем кнопку, если больше нет элементов
                    const noMoreItemsText = document.createElement('p');
                    noMoreItemsText.textContent = 'No more items to load.';
                    noMoreItemsText.style.textAlign = 'center';
                    noMoreItemsText.style.marginTop = '20px';
                    container.appendChild(noMoreItemsText);
                } else {
                    data.content.forEach(product => {
                        const productBox = document.createElement('div');
                        productBox.className = 'product-box';
                        productBox.innerHTML = `
                                <div class="product-image">
                                    <img src="https://via.placeholder.com/300x200" alt="Product Image">
                                </div>
                                <div class="product-info">
                                    <p class="product-name">${product.productName}</p>
                                    <div class="product-price-cart">
                                        <p class="product-price">${product.productPriceCent}</p>
                                        <a href="#" class="add-to-cart">
                                            <img src="img/icons8-тележка-из-магазина-64.png" alt="Add to Cart">
                                        </a>
                                    </div>
                                </div>
                            `;
                        container.appendChild(productBox);
                    });
                    loadMoreButton.setAttribute('data-page', page + 1); // Обновляем атрибут data-page
                }
            });
    });
});