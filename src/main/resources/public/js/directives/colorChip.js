app.directive('colorChip', function(){
    return {
        restrict: 'A',
        scope: {
            chipAdd: "=chipAdd"
        },
        link: function($scope, elem) {
            var chipTemplateClass = $scope.chipAdd ? 'green-chip' : 'red-chip';
            var mdChip = elem.parent().parent();
            mdChip.addClass(chipTemplateClass);
        }
    }
});
