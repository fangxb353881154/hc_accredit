<%--
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%><!DOCTYPE >
<html>
<head>
    <meta charset="utf-8">
    <title>${fns:getConfig('productName')}</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="${ctxStatic}/jingle/css/Jingle.css">
    <link rel="stylesheet" href="${ctxStatic}/jingle/css/app.css">
</head>
<section id="index_section">
    <header>
        <h1 class="title">APP下载</h1>
    </header>
    <article class="active" data-scroll="true">
        <a class="btn btn-primary" href="${ctxStatic}/download/pingwei.ipa_1.0.ipa">点击 下载</a>
    </article>

</section>--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE >
<html>
<head>
    <meta charset="utf-8">
    <title>APP下载</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="${ctxStatic}/jingle/css/Jingle.css">
    <link rel="stylesheet" href="${ctxStatic}/jingle/css/app.css">
</head>
<body>
<div id="aside_container">
</div>
<div id="section_container">
    <section id="login_section" class="active">
        <header>
            <h1 class="title">APP下载</h1>
            <!-- <nav class="right">
                <a data-target="section" data-icon="info" href="#about_section"></a>
            </nav> -->
        </header>
        <article data-scroll="true" class="active">
            <div style="padding: 10px 0 20px;">
                <ul class="list inset demo-list">
                    <li data-icon="next">
                        <button id="btn" class="submit block" data-icon="key" style="text-align: center" onclick="download()">
                            <strong>下载IOS</strong></button>
                    </li>
                </ul>
            </div>
        </article>
    </section>
</div>
<script type="text/javascript">
    function download(tag) {
        var s = navigator.userAgent.toLowerCase();
        if (s.indexOf('iphone') > 0 || s.indexOf('ipad') > 0) {
            if (s.indexOf('micromessenger') > 0 || s.indexOf('qq') > 0) {
                alert('请点击右上角，选择‘在safari中打开’')
            } else {
                location.href = 'itms-services://?action=download-manifest&url=https://raw.githubusercontent.com/fangxb353881154/accredit/master/app.plist'
            }
        } else {
            alert('只能在iPhone或着iPad的浏览器中打开链接')
        }
    }</script>
</body>
</html>