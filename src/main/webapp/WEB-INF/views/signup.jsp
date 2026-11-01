<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>
<div class="row justify-content-center">
    <div class="col-md-8 col-lg-6">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h1 class="h4 mb-3">Create a resident account</h1>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert"><c:out value="${error}"/></div>
                </c:if>
                <form method="post" action="${ctx}/signup">
                    <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label" for="firstName">First name</label>
                            <input class="form-control" id="firstName" name="firstName" maxlength="45"
                                   value="<c:out value='${param.firstName}'/>" required autofocus>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label" for="lastName">Last name</label>
                            <input class="form-control" id="lastName" name="lastName" maxlength="45"
                                   value="<c:out value='${param.lastName}'/>" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="email">Email</label>
                        <input class="form-control" type="email" id="email" name="email" maxlength="150"
                               value="<c:out value='${param.email}'/>" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="password">Password</label>
                        <input class="form-control" type="password" id="password" name="password" minlength="8"
                               maxlength="100" required>
                        <div class="form-text">At least 8 characters, with a letter and a number.</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="confirmPassword">Confirm password</label>
                        <input class="form-control" type="password" id="confirmPassword" name="confirmPassword"
                               maxlength="100" required>
                    </div>
                    <button class="btn btn-primary w-100" type="submit">Create account</button>
                </form>
                <p class="text-center text-muted mt-3 mb-0">
                    Already registered? <a href="${ctx}/login">Sign in</a>
                </p>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
