<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section id="authorKey_section">
    <header>
        <nav class="left">
            <a href="#" data-icon="previous" data-target="back">返回</a>
        </nav>
        <h1 class="title">授权码列表</h1>
    </header>
    <article class="active">
        这是授权码页面!!
    </article>
    <script type="text/javascript">
        $('body').delegate('#user_section','pageinit',function(){
        });
        $('body').delegate('#user_section','pageshow',function(){
            var hash = J.Util.parseHash(location.hash);
            console.log(hash.param);
        });
    </script>
</section>
