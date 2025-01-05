document.addEventListener('DOMContentLoaded', function () {
    const loadMoreButton = document.getElementById('load-more');
    const container = document.getElementById('product-container');

    // Убираем кнопку "Load More", если элементов меньше 12
    const initialProducts = container.querySelectorAll('.product-box').length;
    if (initialProducts < 12) {
        loadMoreButton.style.display = 'none';
    }

    // Функция для обработки кликов на кнопку "Add to Cart"
    container.addEventListener('click', function (event) {
        if (event.target.closest('.add-to-cart')) {
            event.preventDefault();

            const addToCartButton = event.target.closest('.add-to-cart');
            const productId = addToCartButton.getAttribute('data-product-id');
            const productPrice = addToCartButton.getAttribute('data-product-price'); // Считываем цену
            const quantityInput = addToCartButton.closest('.quantity-and-cart').querySelector('.quantity-picker');
            const quantity = quantityInput.value;

            if (!currentUserId) {
                alert('Пожалуйста, авторизуйтесь, чтобы добавить товар в корзину.');
                return;
            }

            if (!productPrice) {
                alert('Ошибка: Цена продукта отсутствует.');
                return;
            }

            // Формируем данные для отправки
            const formData = new URLSearchParams();
            formData.append('productId', productId);
            formData.append('quantity', quantity);
            formData.append('price', productPrice); // Передаём цену
            formData.append('userId', currentUserId.id);

            // Отправляем POST-запрос
            fetch('/addToCart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: formData.toString(),
            })
                .then(response => {
                    if (response.ok) {
                        alert('Товар успешно добавлен в корзину!');
                    } else {
                        alert('Ошибка при добавлении товара в корзину.');
                    }
                })
                .catch(error => console.error('Ошибка:', error));
        }
    });

    // Обработка кнопки "Load More"
    loadMoreButton.addEventListener('click', function () {
        let page = parseInt(loadMoreButton.getAttribute('data-page'));
        let size = parseInt(loadMoreButton.getAttribute('data-size'));

        fetch(`/products/loadMore?page=${page}&size=${size}`)
            .then(response => response.json())
            .then(data => {
                if (data.content.length === 0 || data.content.length < size) {
                    loadMoreButton.style.display = 'none'; // Скрываем кнопку, если больше нет элементов
                } else {
                    data.content.forEach(product => {
                        const productBox = document.createElement('div');
                        productBox.className = 'product-box';
                        productBox.innerHTML = `
                            <div class="product-image">
                                <img src="data:image/jpeg;base64,${product.base64Image}" alt="Product Image">
                            </div>
                            <div class="product-info">
                                <p class="product-name">${product.productName}</p>
                                <p class="product-quantity">Кол-во: ${product.productQuantity}</p>
                                <div class="product-price-cart">
                                    <p class="product-price">Цена: ${product.productPriceCent}</p>
                                    <div class="quantity-and-cart">
                                        <input type="number" min="1" value="1" class="quantity-picker" max="${product.productQuantity}" id="quantity-${product.id}">
                                        <a href="#" class="add-to-cart" data-product-id="${product.id}" data-product-price="${product.productPriceCent}">
                                            <img src="img/icons8-тележка-из-магазина-64.png" alt="Add to Cart">
                                        </a>
                                    </div>
                                </div>
                            </div>
                        `;
                        container.appendChild(productBox);
                    });
                    loadMoreButton.setAttribute('data-page', page + 1); // Увеличиваем страницу
                }
            })
            .catch(error => console.error('Ошибка загрузки:', error));
    });
});
