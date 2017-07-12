

function componentinit(){
    $(".hfmutexcont .box-content .box-header .box-icon").html("");
    $(".hfmutexcont .box-content .box-header").removeClass("box-header");

    $(".hftab .box-content .nav-tabs").each(function(){
        $(this).find("li:first").addClass("active");
    });
    $(".hftab .box-content .tab-content").each(function(){
        $(this).find("div:first").addClass("active");
    });

    $(".hftab  .box-content .nav-tabs a").click(function(b) {
        b.preventDefault();
        $(this).tab("show");
    });

    $(".hflist  .box-content .hflist-data a[when][when!='{}']").each(function(){
        $(this).hide();
       var conditions = JSON.parse( $(this).attr("when"));
        for(var key in conditions) {
            var $span = $(this).parent("td").parent("tr").find("span[code='"+ key +"']");
            var value = conditions[key];
            if("NotBlank" == value && ($span.attr("value") || $span.text())) {
                $(this).show();
            }else if($span.attr("value") == value || $span.text() == value) {
                $(this).show();
            }

        }
    });

    $("div[group][group!='']").each(function(index,element){
        if(index != 0) {
            $(this).hide();
        }
    });

    $(".hflist  .box-content .hflist-data tr td").each(function(){
        var count = 0;
        var $this = $(this);
        $(this).find("a").each(function(){
            if( $(this).attr("when") &&  $(this).attr("when") !="{}") {
                var conditions = JSON.parse( $(this).attr("when"));
                for(var key in conditions) {
                    var $span = $(this).parent("td").parent("tr").find("span[code='"+ key +"']");
                    var value = conditions[key];
                    if($span.attr("value") == value || $span.text() == value) {
                        count = count + 1;
                    }
                }
            }else {
                count = count + 1;
            }
        });
        if(count > 3) {
            $($this).attr("width",count*50 + "px;");
        }
    });
    //$( ".datepicker" ).datepicker({
    //    dateFormat: "yy-mm-dd"
    //});

    $( ".datepicker" ).datetimepicker({
        showSecond: true,
        showMillisec: false,
        timeFormat: 'hh:mm:ss',
        dateFormat: "yy-mm-dd"
    });


    $(".hfTreeList").each(function(){
        var $tree = $(this).find(".tree");
        //$(".tree-folder-header").click();
        var $templateTable = $(this).find($("table"));
        var $templateTableRow =  $templateTable.find(".hflist-data tr:first").clone();

        $templateTableRow.children("td:first").find("select,input").each(function(){
            $templateTableRow.children("td:first").append($("<input name='" + $(this).attr("name") + "' id='" + $(this).attr("id") + "' type='hidden' />"))
            $(this).remove();
        });

        var $table = $templateTable.clone();
        $table.children(".hflist-data").empty();
        $tree.prepend($table);

        var $treeItems = $tree.children("div:visible");
        recursionNestTable($tree, $table, $treeItems, $templateTable, $templateTableRow, 0);
        $templateTable.hide();

        $(this).find('input.switch-checkbox[type="checkbox"], input.switch-radio[type="radio"]').not(".switch-checkbox-not-init")
            .bootstrapSwitch();
        ////样式冲突
        //$(this).find(".bootstrap-switch-container .checker").css("width", "0px");
        //$(this).find(".bootstrap-switch-container .checker").css("margin-right", "0px");

    });


    /**
     * 循环递归 嵌套table
     * @param $table
     * @param $treeItems
     * @param $templateTable
     * @param $templateTableRow
     */
    function recursionNestTable(_$tree, _$table, _$treeItems, _$templateTable, _$templateTableRow, deepIndex){
        _$treeItems.each(function(){
            var $newRow = _$templateTableRow.clone();
            var $contentDiv = $("<div class='tree-folder-content' style='position:relative;margin-left: " + deepIndex * 23 + "px;'></div>");
            var data = $(this).data()["additionalParameters"];
            if($(this).hasClass("tree-folder")) {
                data = $(this).children().data()["additionalParameters"];
            }
            //var data = _$tree.tree('getAdditionalParameters', $(this));
            $contentDiv.append($(this));
            //$newRow.children("td:first").empty();
            $newRow.find("select,input").each(function(){
               if(data[$(this).attr("name")]) {
                   if($(this).is("select")){
                       $(this).attr("data-value", data[$(this).attr("name")]);
                   }else {
                       if($(this).is("[type=checkbox]") || $(this).is("[type=radio]")) {
                           $(this).parents("label.hfcheckbox").attr("data-value", data[$(this).attr("name")]);
                       }else {
                           $(this).val(data[$(this).attr("name")]);
                       }
                   }
                }
            });
            $newRow.children("td:first").append($contentDiv);
            _$table.children(".hflist-data").append($newRow);
            if($(this).hasClass("tree-folder")) {
                var $subTreeItems = $(this).children(".tree-folder-content").children("div:visible");
                recursionNestTable(_$tree, _$table, $subTreeItems, _$templateTable, _$templateTableRow, deepIndex + 1)
                $(this).remove(".tree-folder-content");
            }
        });
    }

    ///**
    // * 循环递归 嵌套table
    // * @param $table
    // * @param $treeItems
    // * @param $templateTable
    // * @param $templateTableRow
    // */
    //function recursionNestTable(_$table, _$treeItems, _$templateTable, _$templateTableRow){
    //    _$treeItems.each(function(){
    //        var $newRow = _$templateTableRow.clone();
    //        $newRow.children(":first").append($(this));
    //        if($(this).hasClass("tree-folder")) {
    //            var $subTable = _$templateTable.clone();
    //            $subTable.children(".hflist-data").empty();
    //            $subTable.children(".hflist-data").append($newRow);
    //            $subTable.children("thead").hide();
    //            var $container = $("<tr><td colspan='100%' style='border: 0px;padding: 0px'></td></tr>");
    //            $container.children(":first").append($subTable)
    //            _$table.children(".hflist-data").append($container);
    //            var $subTreeItems = $(this).children(".tree-folder-content").children("div:visible");
    //            recursionNestTable($subTable, $subTreeItems, _$templateTable, _$templateTableRow)
    //        }else {
    //            _$table.children(".hflist-data").append($newRow);
    //        }
    //    });
    //}

    ///**
    // * 循环递归 嵌套table
    // * @param $table
    // * @param $treeItems
    // * @param $templateTable
    // * @param $templateTableRow
    // */
    //function recursionNestTable(_$table, _$treeItems, _$templateTable, _$templateTableRow){
    //    _$treeItems.each(function(){
    //        var $newRow = _$templateTableRow.clone();
    //        $newRow.children(":first").append($(this));
    //        if($(this).hasClass("tree-folder")) {
    //            var $subTable = _$templateTable.clone();
    //            $subTable.children(".hflist-data").empty();
    //            $subTable.children("thead").hide();
    //            var $container = $("<tr><td colspan='100%' style='border: 0px;padding: 0px'></td></tr>");
    //            $container.children(":first").append($(this));
    //            $(this).children(".tree-folder-content").append($subTable);
    //            //$container.children(":first").append($subTable)
    //            _$table.children(".hflist-data").append($container);
    //            var $subTreeItems = $(this).children(".tree-folder-content").children("div:visible");
    //            recursionNestTable($subTable, $subTreeItems, _$templateTable, _$templateTableRow)
    //        }else {
    //            _$table.children(".hflist-data").append($newRow);
    //        }
    //    });
    //}



}

$.datepicker.regional['ru'] = {
    closeText: '1',
    prevText: 2,
    nextText: 'След&#x3e;',
    currentText: 'Сегодня',
    monthNames: ['一月','二月','三月','四月','五月','六月',
        '七月','八月','九月','十月','十一月','十二月'],
    monthNamesShort: ['Янв','Фев','Мар','Апр','Май','Июн',
        'Июл','Авг','Сен','Окт','Ноя','Дек'],
    dayNames: ['воскресенье','понедельник','вторник','среда','четверг','пятница','суббота'],
    dayNamesShort: ['вск','пнд','втр','срд','чтв','птн','сбт'],
    dayNamesMin: ['日','一','二','三','四','五','六'],
    weekHeader: 'Не',
    dateFormat: 'yy-mm-dd',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: ''
};
$.datepicker.setDefaults($.datepicker.regional['ru']);

$.timepicker.regional['ru'] = {
    timeOnlyTitle: '1',
    timeText: '时间',
    hourText: '时',
    minuteText: '分',
    secondText: '秒',
    millisecText: '毫秒',
    timezoneText: '时区',
    currentText: '当前时间',
    closeText: '确定',
    timeFormat: 'HH:mm',
    amNames: ['AM', 'A'],
    pmNames: ['PM', 'P'],
    isRTL: false
};

$.timepicker.setDefaults($.timepicker.regional['ru']);
componentinit();

