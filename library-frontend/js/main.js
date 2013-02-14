$(function() {
    loadStories();

    $('#search').submit(function() {
        loadStories($('.typeahead').val());
        return false;
    });
});

function loadStories(tag) {
    var tmpl = Handlebars.compile($("#story-template").html());
    $('.loading-indicator').show();
    var url;

    $('.typeahead').val(tag);

    url = "http://localhost:8080/";
    if (tag && tag == "*") {
        url += 'stories/';
    } else if (tag) {
        tag = tag.replace("\/", "%2F");
        url += 'stories/tagged/'+tag;
    } else {
        url += 'stories/tagged/complete'
    }



    $.get(url, function(data) {
        $('.loading-indicator').hide();
        $('.result .row').remove();

        $.each(data, function( index, value ) {
            $('.result').append(tmpl(value));
        });

        $('.story-tag').click(function() {
            loadStories($(this).html());
        });
    });
}

Handlebars.registerHelper('capitalize', function(object) {
    return object.toUpperCase();
});

Handlebars.registerHelper('actionTag', function(object) {
    if ($.inArray(object, ['completed', 'oneshot', 'incomplete', 'complete', 'abandoned']) > -1) {
        return "label-success";
    }
    if (object.indexOf("/") !== -1) {
        return "label-warning"
    }
    return "";
});

Handlebars.registerHelper('toFixed2', function(object) {
    return object.toFixed(1);
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
