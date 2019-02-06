/* * * * * * * * * * * * * * * * *
 * Pagination
 * javascript page navigation
 * * * * * * * * * * * * * * * * */

var Pagination = {

    code: '',

    // --------------------
    // Utility
    // --------------------

    // converting initialize data
    Extend: function(data) {
        data = data || {};
        Pagination.size = data.size || 300;
        Pagination.page = data.page || 1;
        Pagination.step = data.step || 3;
    },

    // add pages by number (from [s] to [f])
    Add: function(s, f) {
        for (var i = s; i < f; i++) {
            Pagination.code += '<li><a href="#" class="number">' + i + '</a></li>';
        }
    },

    // add last page with separator
    Last: function() {
        Pagination.code += '<li><i>...</i><a href="#" class="number">' + Pagination.size + '</a></li>';
    },

    // add first page with separator
    First: function() {
        Pagination.code += '<li><a href="#" class="number">1</a></li><i>...</i>';
    },



    // --------------------
    // Handlers
    // --------------------

    // change page
    Click: function() {
        Pagination.page = +this.innerHTML;
        todo_module.movePage(Pagination.page);
        Pagination.Start();
    },

    // previous page
    Prev: function() {
        Pagination.page--;
        if (Pagination.page < 1) {
            Pagination.page = 1;
        }
        todo_module.movePage(Pagination.page);
        Pagination.Start();
    },

    // next page
    Next: function() {
        Pagination.page++;
        if (Pagination.page > Pagination.size) {
            Pagination.page = Pagination.size;
        }
        todo_module.movePage(Pagination.page);
        Pagination.Start();
    },

    // --------------------
    // Script
    // --------------------

    // binding pages
    Bind: function() {
        var $aArr = $(Pagination.e).find('li a.number'); //getElementsByTagName('a');
        $.each($aArr, function(){
            if($(this).text() == Pagination.page){
                $(this).parent().addClass('active');
            }
            $(this).on('click', Pagination.Click);
        });
        // for (var i = 0; i < a.length; i++) {
        //     if (+a[i].innerHTML === Pagination.page) $(a[i]).parent().addClass('current');
        //     a[i].addEventListener('click', Pagination.Click, false);
        // }
    },

    // write pagination
    Finish: function() {
        $(Pagination.e).find('li.number').replaceWith(Pagination.code);
        Pagination.code = '';
        Pagination.Bind();
    },

    // find pagination type
    Start: function() {
        if (Pagination.size < Pagination.step * 2 + 6) {
            Pagination.Add(1, Pagination.size + 1);
        }
        else if (Pagination.page < Pagination.step * 2 + 1) {
            Pagination.Add(1, Pagination.step * 2 + 4);
            Pagination.Last();
        }
        else if (Pagination.page > Pagination.size - Pagination.step * 2) {
            Pagination.First();
            Pagination.Add(Pagination.size - Pagination.step * 2 - 2, Pagination.size + 1);
        }
        else {
            Pagination.First();
            Pagination.Add(Pagination.page - Pagination.step, Pagination.page + Pagination.step + 1);
            Pagination.Last();
        }
        Pagination.Finish();
    },



    // --------------------
    // Initialization
    // --------------------

    // binding buttons
    Buttons: function(e) {
        var nav = e.getElementsByTagName('a');
        nav[0].addEventListener('click', Pagination.Prev, false);
        nav[1].addEventListener('click', Pagination.Next, false);
    },

    // create skeleton
    Create: function(e) {

        var html = [
            '<nav>',
            '<ul class="pagination pagination-sm">',
            '<li><a href="#" aria-label="Previous"><span aria-hidden="true">«</span></a></li>', // previous button
            '<li class="number"></li>',                                                               // pagination container
            '<li><a href="#" aria-label="Next"><span aria-hidden="true">»</span></a></li>',     // next button
            '</ul>',
            '</nav>'
        ];

        e.innerHTML = html.join('');
        Pagination.e = e;//.getElementsByClassName('number')[0];
        Pagination.Buttons(e);
    },

    // init
    Init: function(e, data) {
        Pagination.Extend(data);
        Pagination.Create(e);
        Pagination.Start();
    }
};