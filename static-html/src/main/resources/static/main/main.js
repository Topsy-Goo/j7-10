
angular.module('market-front').controller('mainController', function ($scope, $http, $rootScope)
{
//просто сборник путей для удобства:
	const contextProductPath 		= 'http://localhost:5555/market/products';
	const contextCartPath	 		= 'http://localhost:5555/market/cart';
	const contextAuthoPath   		= 'http://localhost:5555/market/auth';
	const contextOrderPath	 		= 'http://localhost:5555/market/order';
	const contextProductReviewsPath	= 'http://localhost:5555/market/productreviews';
	const contextUserProfilePath	= 'http://localhost:5555/market/user_profile';
});
