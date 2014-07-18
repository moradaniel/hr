'use strict';

(function () {


	var module = angular.module('admin-employment-edit', [	'services.rest.api',
	                                                      	'services.notifications'
	                                                      	]);

	module.directive('modalEmploymentEditor', ["api","SessionService","LogNotificationService",
	                                           function(api,SessionService,LogNotificationService) {
		return {
			restrict : 'A',
			templateUrl: prefixContextPath +'/resources/javascript/app/admin/employments/admin-employment-editor.html',
			transclude: false,
			controller: function($scope) {
				$scope.modal_id = 'modal_' + Math.floor((Math.random()*100+1));
				$scope.handler = $scope.modal_id;
			},
			scope : {
				handler : '=', //twoway binding 
				'employmentToEdit' : '=employment', //twoway binding employment
				callback: '=callback' //twoway binding 
			},

			link : function ($scope, $element, $attrs) {
				$scope.saveEmployment = function() {
					return api.employments.save($scope.employmentToEdit).then(function (response) {
						LogNotificationService.log('success',"Los cambios a:    "+$scope.employmentToEdit.person.apellidoNombre+"     fueron guardados");
						$scope.callback();

					}, function (reasonError) {

						LogNotificationService.log('error',"Error!");

					});
				};
			}

		};

	}]);


	/**
	 * http://www.benlesh.com/2012/12/angular-js-custom-validation-via.html
	 */
	module.directive('cuilValidate', function() {


		function validate_Cuit_Cuil(sCUIT) 
		{     
			var aMult = '5432765432'; 
			var aMult = aMult.split(''); 

			if (sCUIT && sCUIT.length == 11) 
			{ 
				var aCUIT = sCUIT.split(''); 
				var iResult = 0; 
				for(var i = 0; i <= 9; i++) 
				{ 
					iResult += aCUIT[i] * aMult[i]; 
				} 
				iResult = (iResult % 11); 
				iResult = 11 - iResult; 

				if (iResult == 11) iResult = 0; 
				if (iResult == 10) iResult = 9; 

				if (iResult == aCUIT[10]) 
				{ 
					return true; 
				} 
			}     
			return false; 
		};


		return {
			// restrict to an attribute type.
			restrict: 'A',

			// element must have ng-model attribute.
			require: 'ngModel',

			// scope = the parent scope
			// elem = the element the directive is on
			// attr = a dictionary of attributes on the element
			// ctrl = the controller for ngModel.
			link: function(scope, elem, attr, ctrl) {

				// add a parser that will process each time the value is 
				// parsed into the model when the user updates it.
				ctrl.$parsers.unshift(function(value) {
					// test and set the validity after update.
					var valid = validate_Cuit_Cuil(value);
					ctrl.$setValidity('cuilValidate', valid);

					// if it's valid, return the value to the model, 
					// otherwise return undefined.
					return valid ? value : undefined;
				});

				// add a formatter that will process each time the value 
				// is updated on the DOM element.
				ctrl.$formatters.unshift(function(value) {
					// validate.

					var valid = validate_Cuit_Cuil(value);
					ctrl.$setValidity('cuilValidate', valid);

					// return the value or nothing will be written to the DOM.
					return value;
				});
			}
		};
	});


}());