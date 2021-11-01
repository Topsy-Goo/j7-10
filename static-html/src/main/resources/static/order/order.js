
angular.module('market-front').controller('orderController',
	function ($rootScope, $scope, $http, $location, $localStorage)
{
	const contextOrderPath  = 'http://localhost:5555/market/order';
	const contextPaypalPath = 'http://localhost:5555/market/paypal';

	var cartPageCurrent  = 0;
	var cartPageTotal	= 0;
	$scope.orderNumber   = 0;
	$scope.contextPrompt = "";
	$scope.showForm 	 = true;
	$scope.wellDone 	 = false;
	$scope.canPay   	 = false;

	$scope.loadOrderDetailes = function ()
	{
		$http.get (contextOrderPath + '/details')
		.then (
		function successCallback (response)
		{
			$scope.orderDetails = response.data;
			console.log ($scope.orderDetails);
			if ($scope.orderDetails.cartDto.load <= 0)
			{
				message = 'Заказ пуст.';
				alert (message);
				console.log (message);
				$location.path('/cart')
			}
			else
			{	$scope.contextPrompt = "Ваш заказ сформирован.";
				$scope.cart = $scope.orderDetails.cartDto;
				console.log ('Детали заказа загружены:');
				console.log (response.data);
			}
		},
		function failureCallback (response)
		{
			$scope.contextPrompt = "Произошла ошибка!";
			alert (response.data);
		});
	}

	$scope.confirmOrder = function ()
	{
		if ($scope.orderDetails.cartDto.load > 0)
		$http
		({	url:	contextOrderPath + '/confirm',
			method:	'POST',
			data:	$scope.orderDetails
		})
		.then(
		function successCallback (response)
		{
			$scope.showForm = false;
			$scope.wellDone = true;
			$scope.canPay   = true;
			$scope.contextPrompt = 'Ваш заказ оформлен.';
			alert ($scope.contextPrompt);
			$scope.orderDetails = response.data;
			console.log ('Заказ оформлен.');
			console.log ($scope.orderDetails);
		},
		function failureCallback (response)
		{
			$scope.contextPrompt = "Произошла ошибка! ";
			console.log (response.data.messages);
			alert ($scope.contextPrompt + response.data.messages);
			/* если выводим сообщение от валидатора, то нужно укзаывать имя поля с сообщением, например:	response.data.messages,
			а для всех нормальных сообщений — указываем только response.data.	*/
		});
	}

/* Если кнопки не показываются или не работают, то, возможно, помогут следующие меры:
	- добавить localhost (или localhost:5555) в белый список AdblockPlus;
	- запретить антивирусу открывать сайты
			www.paypal.com,
			www.sandbox.paypal.com,
			developer.paypal.com
	  в защищённом браузере;
	- разрешить антивирусу сбор данных на сайте localhost:5555 или на стр.оформления заказа;
	- отключить антибаннер(ы) на странице оформления заказа.
	P.S. Если это вам покажется слишком геморройным для простого проекта с двумя кнопками, то значит вы не любите программировать.
*/
/*	the server responded with a status of 499	-	Касперский
	net::ERR_BLOCKED_BY_CLIENT	-	AdblockPlus
	Document is ready and element #paypal-buttons does not exist	-	кнопка не отрисована ко времени, когда она нужна какому-то скрипту (отрисовку может задержать какая-то ошибка или код страницы);
*/
	$scope.renderPaymentButtons = function()
	{
		$scope.canPay = false;
		console.log ('Показ кнопки PayPal.');		console.log ($scope.orderDetails);
		paypal.Buttons(
		{
			createOrder: function(data, actions)
			{
				return fetch(contextPaypalPath + '/create/' + $scope.orderDetails.orderNumber,
				{	method: 'post',
					headers: {'content-type': 'application/json'}
				})
				.then(function(response) {return response.text();});
			},
			onApprove: function(data, actions)
			{
				return fetch(contextPaypalPath + '/capture/' + data.orderID,
				{	method: 'post',
					headers: {'content-type': 'application/json'}

					$scope.orderDetails.orderState = 'Оплачен'; //Это для описания заказа на текущей стр.
				})
				.then(function(response)
				{	response.text().then(msg => alert(msg));
					/* Статус заказа изменяется на «оплачен» в PayPalController перед самым возвратом из обработчика …/api/v1/paypal/create/{payPalId}. */
				});
			},
			onCancel: function (data) {console.log ("Order canceled: " + data);},
			onError:  function (err)  {console.log (err);}
		})
		.render('#paypal-buttons');
	}

	$scope.cancelOrdering = function () { $location.path('/cart'); }

	$scope.ok = function () { $location.path('/store'); }
//----------------------------------------------------------------------- действия
	$scope.infoProduct = function (oitem)
	{
		alert('id:			  '+ oitem.productId +
		   ',\rкатегория:	   '+ oitem.category +
		   ',\rназвание:		'+ oitem.title +
		   ',\rцена:			'+ oitem.price +
		   ',\rколичество:	  '+ oitem.quantity +
		   ',\rобщая стоимость: '+ oitem.cost);
	}
//----------------------------------------------------------------------- условия
	$scope.canShowConfirmationButton = function ()	{ return $rootScope.isUserLoggedIn(); }

	$scope.canShowOrderedItems = function ()	{ return $rootScope.isUserLoggedIn(); }
//----------------------------------------------------------------------- вызовы
	$scope.loadOrderDetailes();
});