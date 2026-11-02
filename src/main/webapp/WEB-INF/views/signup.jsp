<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Create Account | SmartCity360</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        primary: { 50:'#eef2ff', 100:'#e0e7ff', 500:'#6366f1', 600:'#4f46e5', 700:'#4338ca' }
                    }
                }
            }
        }
    </script>
</head>
<body class="min-h-screen bg-slate-100 flex items-center justify-center p-4">

<div class="w-full max-w-lg">
    <div class="bg-white rounded-2xl shadow-xl overflow-hidden">
        <!-- Header -->
        <div class="bg-gradient-to-r from-indigo-600 to-indigo-500 px-8 py-7 text-center text-white">
            <div class="flex items-center justify-center gap-2 text-xl font-bold">
                <i class="bi bi-buildings text-2xl"></i>
                <span>SmartCity360</span>
            </div>
            <p class="text-indigo-100 text-sm mt-1">Create a Resident Account</p>
        </div>

        <div class="px-8 py-8">
            <h1 class="text-xl font-semibold text-slate-800 text-center">Create your account</h1>
            <p class="text-sm text-slate-500 text-center mt-1 mb-6">Join the SmartCity360 platform as a resident.</p>

            <c:if test="${not empty error}">
                <div class="mb-5 flex items-center gap-2 rounded-lg bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <span><c:out value="${error}"/></span>
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/signup" id="signupForm" class="space-y-4">
                <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">

                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-1.5" for="firstName">First Name</label>
                        <input class="w-full rounded-xl border border-slate-200 bg-slate-50 py-2.5 px-3.5 text-sm
                                     focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 outline-none"
                               id="firstName" name="firstName" maxlength="45"
                               value="<c:out value='${param.firstName}'/>" required autofocus>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-1.5" for="lastName">Last Name</label>
                        <input class="w-full rounded-xl border border-slate-200 bg-slate-50 py-2.5 px-3.5 text-sm
                                     focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 outline-none"
                               id="lastName" name="lastName" maxlength="45"
                               value="<c:out value='${param.lastName}'/>" required>
                    </div>
                </div>

                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1.5" for="email">Email Address</label>
                    <div class="relative">
                        <i class="bi bi-envelope absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"></i>
                        <input type="email" id="email" name="email" maxlength="150"
                               value="<c:out value='${param.email}'/>" required
                               class="w-full rounded-xl border border-slate-200 bg-slate-50 py-2.5 pl-11 pr-3.5 text-sm
                                      focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 outline-none">
                    </div>
                </div>

                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1.5" for="buildingId">Your Building</label>
                    <select id="buildingId" name="buildingId" required
                            class="w-full rounded-xl border border-slate-200 bg-slate-50 py-2.5 px-3.5 text-sm
                                   focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 outline-none">
                        <option value="">— Select your building —</option>
                        <c:forEach var="b" items="${buildings}">
                            <option value="${b.value}" ${b.value eq param.buildingId ? 'selected' : ''}>
                                <c:out value="${b.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1.5" for="password">Password</label>
                    <div class="relative">
                        <i class="bi bi-lock absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"></i>
                        <input type="password" id="password" name="password" minlength="8" maxlength="100" required
                               oninput="checkStrength(this.value)"
                               class="w-full rounded-xl border border-slate-200 bg-slate-50 py-2.5 pl-11 pr-3.5 text-sm
                                      focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 outline-none">
                    </div>
                    <p class="text-xs text-slate-400 mt-1">At least 8 characters.</p>
                    <div id="strengthBar" class="mt-2 h-1 rounded-full bg-slate-200 overflow-hidden">
                        <div id="strengthFill" class="h-full rounded-full transition-all duration-300" style="width:0"></div>
                    </div>
                    <div id="strengthLabel" class="text-xs text-slate-500 mt-1"></div>
                </div>

                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1.5" for="confirmPassword">Confirm Password</label>
                    <div class="relative">
                        <i class="bi bi-lock-fill absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"></i>
                        <input type="password" id="confirmPassword" name="confirmPassword" maxlength="100" required
                               class="w-full rounded-xl border border-slate-200 bg-slate-50 py-2.5 pl-11 pr-3.5 text-sm
                                      focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 outline-none">
                    </div>
                    <div id="matchFeedback" class="text-xs mt-1"></div>
                </div>

                <button type="submit"
                        class="w-full flex items-center justify-center gap-2 rounded-xl bg-indigo-600 hover:bg-indigo-700
                               text-white font-semibold py-3 text-sm shadow-sm transition mt-2">
                    <i class="bi bi-person-check"></i>
                    Create Account
                </button>
            </form>

            <p class="mt-6 text-center text-sm text-slate-500">
                Already have an account?
                <a href="${pageContext.request.contextPath}/login" class="font-semibold text-indigo-600 hover:text-indigo-700">
                    Sign in
                </a>
            </p>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/signup.js"></script>
</body>
</html>