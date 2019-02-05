$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * 할일 (TODO) 를 위한 공통
 * @type {{init, movePage, saveJob}}
 */
var todo_module = todo_module || (function() {
    /**
     * AJAX 결과를 JSON으로 받는다.
     * @param newOptions
     */
    var getCustomJson = function(newOptions){
        var options = {
            type: "GET",
            dataType: "JSON",
            error: commonError
        };
        $.extend(options, newOptions);
        $.ajax({
            url: options.url,
            data: options.data,
            success:function(data, status, settings){
                if(options.success){
                    options.success(data, status, settings);
                }
            },
            error:function(ajaxrequest, ajaxOptions, thrownError){
                options.error(ajaxrequest, ajaxOptions, thrownError);
            }
        })
    };

    /**
     * POST로 JSON 데이터를 전송한다.
     * @param newOptions
     */
    var postCustomJson = function(newOptions){
        var options = {
            type: "POST",
            dataType: "JSON",
            contentType: "application/json",
            error: commonError
        };
        $.extend(options, newOptions);
        $.ajax({
            url: options.url,
            type: options.type,
            data: options.data,
            contentType: options.contentType,
            success:function(data, status, settings){
                if(options.success){
                    options.success(data, status, settings);
                }
            },
            error:function(ajaxrequest, ajaxOptions, thrownError){
                options.error(ajaxrequest, ajaxOptions, thrownError);
            }
        });
    };

    /**
     * PUT로 JSON 데이터를 전송한다.
     * @param options
     */
    var putCustomJson = function(newOptions){
        var options = {
            type: "PUT",
            dataType: "JSON",
            contentType: "application/json",
            error: commonError
        };
        $.extend(options, newOptions);
        $.ajax({
            url: options.url,
            type: options.type,
            data: options.data,
            contentType: options.contentType,
            success:function(data, status, settings){
                if(options.success){
                    options.success(data, status, settings);
                }
            },
            error:function(ajaxrequest, ajaxOptions, thrownError){
                options.error(ajaxrequest, ajaxOptions, thrownError);
            }
        });
    };

    /**
     * 공통 에러 처리.
     * @param ajaxrequest
     * @param ajaxOptions
     * @param thrownError
     */
    var commonError = function(ajaxrequest, ajaxOptions, thrownError){
        if(ajaxrequest.responseJSON){
            alert("[" + ajaxrequest.responseJSON.code + "] " + ajaxrequest.responseJSON.message);
        }
    };

    /**
     * 전체 할일 리스트를 가져와 체크박스를 생성한다.
     */
    var loadJobListCheckbox = function(){
        getCustomJson({
            url: "/api/v1/todo-list/ids",
            success: function(data){
                //기존 값을 지우고 새로 받아온 데이터로 채운다!
                $('#ul-job-checkbox').empty();
                $.each(data.datas, function(index, item){
                    $('#ul-job-checkbox').append("<li><a href='#' class='small' data-value='" + item + "' tabIndex='-1'><input type='checkbox'/>" + item + "</a></li>");
                });
            }
        });
    };

    /**
     * 저장 버튼을 누를 경우.
     */
    var saveJob = function(){
        var jobId = $('#regist-form').find('#jobId').val();
        // 수정
        if(jobId !== ''){
            updateJob();
        }
        // 등록
        else{
            addJob();
        }
    }

    /**
     * 할일을 추가한다.
     */
    var addJob = function(){
        var param = $('#regist-form').serializeObject();
        var listReferJobId = param.strListReferJobId.split(',');
        if(param.strListReferJobId != '' && listReferJobId.length > 0){
            param.listReferJobId = listReferJobId;
        }

        postCustomJson({
            url: "/api/v1/todo-list/job",
            data: JSON.stringify(param),
            success: function(data, status, settings){
                alert("등록이 완료 되었습니다.");
                $('#modal-job').modal('hide');
                movePage(1);
            }
        });
    };

    /**
     * 할일을 수정한다.
     */
    var updateJob = function(){
        var param = $('#regist-form').serializeObject();
        var listReferJobId = param.strListReferJobId.split(',');
        if(param.strListReferJobId != '' && listReferJobId.length > 0){
            param.listReferJobId = listReferJobId;
        }

        putCustomJson({
            url: "/api/v1/todo-list/job/" + param.jobId,
            data: JSON.stringify(param),
            success: function(data, status, settings){
                alert("등록이 완료 되었습니다.");
                $('#modal-job').modal('hide');
                var pageNum = $('#paramForm').find('input[name=pageNum]').val();
                movePage(pageNum);
            }
        });
    };

    /**
     * 완료 여부 변경에 따른 처리를 한다.
     */
    var changeCompleteYn = function(){
        var isChecked = $(this).is(":checked");
        var completeYn = isChecked? "Y" : "N";
        var jobId = $(this).val();

        var param = {};
        param.jobId = jobId;
        param.completeYn = completeYn;

        var $that = $(this);

        putCustomJson({
            url: "/api/v1/todo-list/complete/" + param.jobId,
            data: JSON.stringify(param),
            success: function(data, status, settings){
                if(data == 'Y'){
                    alert("변경 완료 되었습니다.");
                }else{
                    if(completeYn == 'Y'){
                        alert("참조한 할일이 완료되지 않았습니다.");
                        $that.prop('checked', false);
                    }else{
                        alert("해당 할일을 참조한 일감이 이미 완료 상태로 변경 되어 완료 해제가 불가능 합니다.");
                        $that.prop('checked', true);
                    }
                    return false;
                }
            }
        });
    };

    /**
     * 페이지네이션을 생성한다.
     * @param totalCnt
     * @param pageNum
     * @param targetId
     */
    var createPagination = function(totalCnt, pageNum, targetId){
        var pagePerCnt = 10;
        var o = totalCnt % pagePerCnt;
        var pageCnt = totalCnt / pagePerCnt;
        if(o > 0){
            pageCnt = pageCnt + 1;
        }

        Pagination.Init(document.getElementById(targetId), {
            size: pageCnt,
            page: pageNum,
            step: 3
        });
    };

    /**
     * 페이지를 이동한다.
     * @param pageNum
     */
    var movePage = function(pageNum){
        console.log("####[movePage] page num: %d", pageNum);
        $('#paramForm').find('input[name=pageNum]').val(pageNum);

        getCustomJson({
            url: "/api/v1/todo-list/" + pageNum,
            success: function(data){
                //기존 값을 지우고 새로 받아온 데이터로 채운다!
                $('#jobs').empty();
                $.each(data.datas, function(index, item){
                    var source = $("#job-template").html();
                    var template = Handlebars.compile(source);
                    var strListReferJobId = item.listReferJobId.join(',');
                    item.strListReferJobId = strListReferJobId;
                    item.isComplete = (item.completeYn == 'Y'? true: false);
                    var html = template(item);
                    $('#jobs').append(html);
                });
                createPagination(data.totalCnt, pageNum, "pagination");
            }
        });
    };

    /**
     * 초기화를 위함.
     */
    var init = function(){
        //alert("Init()!");
        movePage(1);
        loadJobListCheckbox();

        /**
         * BOOTSTRAP 드랍다운 체크박스 체크 시 상태 유지를 위함.
         */
        $(document).on('click', '.dropdown-menu a', function( event ) {
            var $target = $( event.currentTarget ),
                $inp = $target.find( 'input' );

            if ($inp.is(':checked')) {
                $inp.prop('checked', false);
            } else {
                $inp.prop('checked', true);
            }
            $( event.target ).blur();

            var checkedVals = $('#ul-job-checkbox').find('input[type=checkbox]').filter(function(){
                return $(this).is(':checked');
            }).map(function(){
                return $(this).parent().data('value');
            }).get().join(',');

            $('#strListReferJobId').val(checkedVals);
            return false;
        });

        /**
         * 추가 버튼을 눌렀을 경우 빈값으로.
         */
        $('#btn-open-add-modal').on('click', function(){
            // JobId로 수정, 추가를 구분하기 때문에 값을 초기화한다.
            $('#regist-form').find('#jobId').val('');
            $('#regist-form').find('#jobContent').val('');
            $('#regist-form').find('input[name=strListReferJobId]').val('');
            $('#modal-job').modal('show');
        });

        /**
         * 수정 버튼을 눌렀을 경우 기존 값을 채운다.
         * -- 동적으로 추가되는 버튼이므로 상위 요소에 바인딩
         */
        $(document).on('click', '#jobs .btn-update', function(){
            var jobId = $(this).data('job-id');
            var jobContent = $(this).parent().parent().find('div .div-content').text();
            var strListReferJobId = $(this).closest('div').find('input[name=strListReferJobId]').val();

            console.log("strListReferJobId: %s", strListReferJobId);

            $('#regist-form').find('#jobId').val(jobId);
            $('#regist-form').find('#jobContent').val($.trim(jobContent));
            $('#regist-form').find('input[name=strListReferJobId]').val(strListReferJobId);

            var listReferJobId = strListReferJobId.split(',');
            if(listReferJobId.length > 0){
                $('#ul-job-checkbox').find('input[type=checkbox]').each(function(){
                    if(listReferJobId.indexOf($(this).parent().data('value')) > -1){
                        $(this).prop('checked', true);
                    }else{
                        $(this).prop('checked', false);
                    }
                });
            }else{
                $('#ul-job-checkbox').find('input[type=checkbox]').prop('checked', false);
            }
            $('#modal-job').modal('show');
        });

        // 완료 체크박스 체크에 따른 액션
        $(document).on('click', '#jobs :input[type=checkbox]', changeCompleteYn);

        /**
         * 모달에서 저장을 누를 경우
         */
        $('#modal-job .modal-footer button').on('click', todo_module.saveJob);
    };

    return {
        init : init,
        movePage : movePage,
        saveJob : saveJob
    };
}());