<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>
<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h1 class="h4 mb-3">Report a problem</h1>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert"><c:out value="${error}"/></div>
                </c:if>
                <form method="post" action="${ctx}/app/complaints/new">
                    <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label" for="categoryId">Category</label>
                            <select class="form-select" id="categoryId" name="categoryId" required>
                                <option value="">Choose...</option>
                                <c:forEach var="o" items="${categories}">
                                    <option value="${o.value}" ${o.value eq param.categoryId ? 'selected' : ''}><c:out value="${o.label}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label" for="zoneId">Where is it?</label>
                            <select class="form-select" id="zoneId" name="zoneId" required>
                                <option value="">Choose...</option>
                                <c:forEach var="o" items="${zones}">
                                    <option value="${o.value}" ${o.value eq param.zoneId ? 'selected' : ''}><c:out value="${o.label}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <fieldset class="mb-3">
                        <legend class="form-label fs-6">How urgent is it?</legend>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="urgency" id="urgMinor" value="MINOR"
                                   ${(empty param.urgency or param.urgency eq 'MINOR') ? 'checked' : ''}>
                            <label class="form-check-label" for="urgMinor">
                                <strong>Minor</strong> <span class="text-muted">- routine. Handled by local staff.</span>
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="urgency" id="urgSerious" value="SERIOUS"
                                   ${param.urgency eq 'SERIOUS' ? 'checked' : ''}>
                            <label class="form-check-label" for="urgSerious">
                                <strong>Serious</strong> <span class="text-muted">- needs a supervisor.</span>
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="urgency" id="urgCritical" value="CRITICAL"
                                   ${param.urgency eq 'CRITICAL' ? 'checked' : ''}>
                            <label class="form-check-label" for="urgCritical">
                                <strong>Critical</strong> <span class="text-muted">- danger or major damage. Goes to the city manager.</span>
                            </label>
                        </div>
                    </fieldset>

                    <div class="mb-3">
                        <label class="form-label" for="description">Describe the problem</label>
                        <textarea class="form-control" id="description" name="description" rows="5"
                                  maxlength="${maxDescription}" required><c:out value="${param.description}"/></textarea>
                        <div class="form-text">Up to ${maxDescription} characters.</div>
                    </div>
                    <button class="btn btn-primary" type="submit">Submit complaint</button>
                    <a class="btn btn-link" href="${ctx}/app/my-complaints">Cancel</a>
                </form>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
