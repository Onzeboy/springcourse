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
            const productPrice = addToCartButton.getAttribute('data-product-price');
            const quantityInput = addToCartButton.closest('.quantity-and-cart').querySelector('.quantity-picker');
            let quantity = parseInt(quantityInput.value, 10);
            const maxQuantity = Math.min(
                parseInt(quantityInput.getAttribute('data-max-quantity'), 10),
                50 // Ограничение на 50 штук
            );

            // Проверяем авторизацию
            if (!currentUserId) {
                alert('Пожалуйста, авторизуйтесь, чтобы добавить товар в корзину.');
                return;
            }

            // Проверяем корректность введенного количества
            if (quantity < 1 || quantity > maxQuantity) {
                alert(`Введите корректное количество (от 1 до ${maxQuantity}).`);
                return;
            }

            // Формируем данные для отправки
            const formData = new URLSearchParams();
            formData.append('productId', productId);
            formData.append('quantity', quantity);
            formData.append('price', productPrice);

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

                        // Обновляем количество на складе
                        const productQuantitySpan = addToCartButton
                            .closest('.product-info')
                            .querySelector('.product-quantity span');
                        const currentQuantity = parseInt(productQuantitySpan.textContent, 10);

                        const newQuantity = currentQuantity - quantity;
                        productQuantitySpan.textContent = newQuantity;

                        // Если товара больше нет, блокируем добавление в корзину
                        if (newQuantity <= 0) {
                            quantityInput.setAttribute('disabled', 'true');
                            addToCartButton.classList.add('disabled');
                            addToCartButton.style.pointerEvents = 'none';
                            alert('Товар закончился на складе.');
                        }
                    } else {
                        response.text().then(errorMessage => {
                            alert(`Ошибка: ${errorMessage}`);
                        });
                    }
                })
                .catch(error => console.error('Ошибка:', error));
        }
    });

    // Обработка кнопки "Load More"
    loadMoreButton.addEventListener('click', function () {
        let page = parseInt(loadMoreButton.getAttribute('data-page'), 10);
        let size = parseInt(loadMoreButton.getAttribute('data-size'), 10);

        fetch(`/products/loadMore?page=${page}&size=${size}`)
            .then(response => response.json())
            .then(data => {
                if (data.content.length === 0 || data.content.length < size) {
                    loadMoreButton.style.display = 'none';
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
                                <p class="product-desc">${product.productDescription}"</p>
                                <p class="product-quantity">Кол-во на складе: ${product.productQuantity}</p>
                                <div class="product-price-cart">
                                    <p class="product-price">Цена: ${product.productPriceCent / 100.0}</p>
                                    <div class="quantity-and-cart">
                                        <input type="number" min="1" value="1" max="${Math.min(product.productQuantity, 50)}" class="quantity-picker">
                                        <a href="#" class="add-to-cart" data-product-id="${product.id}" data-product-price="${product.productPriceCent}">
                                            <img src="img/icons8-тележка-из-магазина-64.png" alt="Add to Cart">
                                        </a>
                                        <button class="btn btn-primary btn-sm edit-product" 
                                                data-product-id="${product.id}" 
                                                data-product-name="${product.productName}" 
                                                data-product-description="${product.productDescription}" 
                                                data-product-quantity="${product.productQuantity}" 
                                                data-product-price="${product.productPriceCent}">
                                            Редактировать
                                        </button>
                                    </div>
                                </div>
                            </div>
                        `;
                        container.appendChild(productBox);
                    });
                    loadMoreButton.setAttribute('data-page', page + 1);
                }
            })
            .catch(error => console.error('Ошибка загрузки:', error));
    });

    // Обработчик клика на кнопку "Редактировать"
    container.addEventListener('click', function (event) {
        if (event.target.closest('.edit-product')) {
            const editButton = event.target.closest('.edit-product');
            const productId = editButton.getAttribute('data-product-id');
            const productName = editButton.getAttribute('data-product-name');
            const productDescription = editButton.getAttribute('data-product-description');
            const productQuantity = editButton.getAttribute('data-product-quantity');
            const productPrice = editButton.getAttribute('data-product-price');

            // Заполняем данные в модальном окне
            document.getElementById('edit-product-id').value = productId;
            document.getElementById('edit-product-name').value = productName;
            document.getElementById('edit-product-description').value = productDescription;
            document.getElementById('edit-product-quantity').value = productQuantity;
            document.getElementById('edit-product-price').value = productPrice;

            // Показываем модальное окно
            const editProductModal = new bootstrap.Modal(document.getElementById('editProductModal'));
            editProductModal.show();
        }
    });
});
