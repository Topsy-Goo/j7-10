
angular.module('market-front').controller('product_pageController',
function ($rootScope, $scope, $http, $location, $routeParams)
{
	const contextProductPath 		= 'http://localhost:5555/market/products';
	const contextAuthoPath	 		= 'http://localhost:5555/market/auth';
	const contextCartPath	 		= 'http://localhost:5555/market/cart';
	const contextOrderPath   		= 'http://localhost:5555/market/order';
	const contextProductReviewsPath = 'http://localhost:5555/market/productreviews';//

	$scope.showReviewForm = false;
	$scope.reviewHalfHeader = 'Пока нет отзывов';
	var failedToLoadProductDescription = 'Не удалось загрузить свойства товара.';
	var failedToLoadReviews = 'Не удалось загрузить отзывы о товаре.';

	$scope.loadProductProperties = function ()
	{
		$http.get (contextProductPath + '/' + $routeParams.pid)
		.then (
		function successCallback (response)
		{
			$scope.productDto = response.data;
			$scope.contextPrompt = response.data.title;
			console.log ('загружены свойства товара id : '+ $routeParams.pid);
		},
		function failureCallback (response)
		{
			$scope.contextPrompt = failedToLoadProductDescription;
		});
	}

	$scope.loadReviews = function ()
	{
		$http.get (contextProductReviewsPath + '/load_reviews/' + $routeParams.pid)
		.then (
		function successCallback (response)
		{
			$scope.prodReviews = response.data;
			if ($scope.prodReviews.length > 0)
				$scope.reviewHalfHeader = 'Отзывы наших покупателей';
		},
		function failureCallback (response)
		{
			$scope.contextPrompt = failToLoadReviews;
			alert (failToLoadReviews +'\r'+ response.data/*.messages*/);
		});
	}
//----------------------------------------------------------------------- действия
	$scope.appendReview = function ()
	{
		$scope.review.productId = $routeParams.pid;
		$scope.review.authorName = null;
		$scope.review.date = null;
		console.log ($scope.review);

		$http.post (contextProductReviewsPath + '/new_review', $scope.review)
		.then (
		function successCallback (response)
		{
			alert ('Спасибо за отзыв. Ваш отзыв опубликован.');
			$scope.canReview();
			$scope.loadReviews();
		},
		function failureCallback (response)
		{
			alert ('К сожалению, ваш отзыв опубликовать не удалось.');
		});
	}
//----------------------------------------------------------------------- условия
	$scope.canReview = function ()
	{
		if (!$rootScope.isUserLoggedIn())
		{
			$scope.showReviewForm = false;
		}
		else
		{
			$http.get (contextProductReviewsPath + '/can_review/' + $routeParams.pid)
			.then (
			function successCallback (response)
			{
				$scope.showReviewForm = response.data;
			},
			function failureCallback (response)
			{
				$scope.showReviewForm = false;
			});
		}
	}
//----------------------------------------------------------------------- вызовы
	$scope.loadProductProperties();
	$scope.loadReviews();
	$scope.canReview();
});