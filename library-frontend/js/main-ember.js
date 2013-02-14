//$(function() {
//    var tmpl = Handlebars.compile($("#story-template").html());
//    $.get('http://localhost:8080/stories/tagged/complete', function(data) {
//        $('.loading-indicator').remove();
//        $.each(data, function( index, value ) {
//            $('.result').append(tmpl(value));
//        });
//    });
//
//
//});
//
//Handlebars.registerHelper('capitalize', function(object) {
//    return object.toUpperCase();
//});
//
//Handlebars.registerHelper('actionTag', function(object) {
//    if ($.inArray(object, ['completed', 'oneshot', 'incomplete', 'complete', 'abandoned']) > -1) {
//        return "label-success";
//    }
//    return "";
//});
window.App = Ember.Application.create();

App.ApplicationController = Ember.Controller.extend({
    firstName: "Trek",
    lastName: "Glowacki"
});
//
//App = Ember.Application.create();
//
//App.ApplicationView = Ember.View.extend({
//    templateName: 'story-template'
//});
//App.ApplicationController = Ember.Controller.extend();
//
//
//App.Router = Ember.Router.extend({
//    root: Ember.Route.extend({
//        index: Ember.Route.extend({
//            route: '/'
//        })
//    })
//});
//
