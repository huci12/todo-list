<%--
  Created by IntelliJ IDEA.
  User: ppyong
  Date: 2019. 2. 1.
  Time: 오후 2:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 위 3개의 메타 태그는 *반드시* head 태그의 처음에 와야합니다; 어떤 다른 콘텐츠들은 반드시 이 태그들 *다음에* 와야 합니다 -->

    <title>Todo 리스트</title>
    <!-- 합쳐지고 최소화된 최신 CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

    <!-- 부가적인 테마 -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

    <%--<link rel="stylesheet" href="/static/css/pagination.css">--%>

    <!-- IE8 에서 HTML5 요소와 미디어 쿼리를 위한 HTML5 shim 와 Respond.js -->
    <!-- WARNING: Respond.js 는 당신이 file:// 을 통해 페이지를 볼 때는 동작하지 않습니다. -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- 핸들바 -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>

    <!-- Jquery -->
    <script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="/static/js/pagination.js"></script>
    <script src="/static/js/todo.js"></script>

    <style>
        h1 {
            display: inline-block;
            color: #292b33;
        }
        body {
            background-color: #f6f6f6;
        }
        #main {
            margin-top: 20px;
            padding-top: 10px;
            background-color: #ffffff;
            border-radius: 5px;
            border: 1px solid #545454;
        }
        #jobs > div {
            border: 1px solid #ddd;
        }
        #footer {
            text-align: center;
        }
        .div-header {
            margin-top: 10px;
            margin-bottom: 10px;
        }
        .div-content-wrap {
            margin-top: 10px;
            margin-bottom: 10px;
        }
        .div-content {
            word-break: break-all;
        }
        .div-date {
            text-align: right;
            margin-top: 3px;
            margin-bottom: 3px;
        }
        .modal-body div.row {
            margin-top: 5px;
        }
    </style>

    <script id="job-template" type="text/x-handlebars-template">
        <div class="row">
            <div class="col-xs-12">
                <div class="row div-content-wrap">
                    <div class="col-xs-11">
                        <div class="row">
                            <div class="col-xs-2">
                                {{jobId}}
                            </div>
                            <div class="col-xs-8">
                                <div class="div-content">
                                    {{jobContent}}
                                </div>
                                <div class="div-label">
                                {{#listReferencedJobId}}
                                    <span class="label label-danger">{{this}}</span>
                                {{/listReferencedJobId}}
                                </div>
                            </div>
                            <div class="col-xs-2">
                                <%--<button type="button" class="btn btn-primary btn-xs btn-delete">삭제</button>--%>
                                <button type="button" data-job-id="{{jobId}}" class="btn btn-primary btn-xs btn-update">수정</button>
                                <input type="hidden" name="strListReferJobId" value="{{strListReferJobId}}">
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-1">
                        <div>
                            <label>
                                <input type="checkbox" {{#if isComplete}} checked="checked" {{/if}} value="{{jobId}}">
                            </label>
                        </div>
                    </div>
                </div>
                <div class="row div-date">
                    <div class="col-xs-12">
                        등록일: {{regDate}}
                    </div>
                    <div class="col-xs-12">
                        수정일: {{updDate}}
                    </div>
                </div>
            </div>
        </div>
    </script>

    <script>
        $(function(){
            todo_module.init();
        });
    </script>
</head>
<body>
    <form id="paramForm">
        <input type="hidden" name="pageNum">
    </form>
    <div class="container" id="main">
        <div id="header" class="row">
            <div class="col-xs-12 div-header" align="right">
                <button type="button" id="btn-open-add-modal" class="btn btn-primary">추가</button>
            </div>
        </div>
        <div id="body" class="row">
            <div id="jobs" class="col-xs-12">
            </div>
        </div>
        <div id="footer">
            <div id="pagination" class="row"></div>
        </div>
    </div>

    <div id="modal-job" class="modal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">할일 등록/수정</h4>
                </div>
                <div class="modal-body">
                    <form id="regist-form">
                        <div class="row">
                            <div class="col-xs-12">
                                할일
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <input type="text" autocomplete="disabled" class ="form-control" id="jobContent" name="jobContent" placeholder="할일을 입력하세요.">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                참조
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <input type="text" autocomplete="disabled" readonly="readonly" class ="form-control" id="strListReferJobId" name="strListReferJobId" placeholder="참조 일감">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
                                    일감
                                    <span class="caret"></span>
                                </button>
                                <ul id="ul-job-checkbox" class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"></ul>
                            </div>
                        </div>
                        <input type="hidden" id="jobId" name="jobId" value="">
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" id="btn-save" class="btn btn-primary">저장</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 합쳐지고 최소화된 최신 자바스크립트 -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</body>
</html>
