<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign In | SmartCity360</title>
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

<div class="w-full max-w-md">
    <!-- Card -->
    <div class="bg-white rounded-2xl shadow-xl overflow-hidden">
        <!-- Header -->
        <div class="bg-gradient-to-r from-indigo-600 to-indigo-500 px-8 py-7 text-center text-white">
            <div class="flex items-center justify-center gap-2 text-xl font-bold">
                <i class="bi bi-buildings text-2xl"></i>
                <span>SmartCity360</span>
            </div>
            <p class="text-indigo-100 text-sm mt-1">Urban Intelligence Platform</p>
        </div>

        <!-- Body -->
        <div class="px-8 py-8">
            <h1 class="text-xl font-semibold text-slate-800 text-center">Sign in to your account</h1>
            <p class="text-sm text-slate-500 text-center mt-1 mb-6">Welcome back! Please enter your details.</p>

            <c:if test="${not empty error}">
                <div class="mb-5 flex items-center gap-2 rounded-lg bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <span><c:out value="${error}"/></span>
                </div>
            </c:if>
            <c:if test="${not empty flashMessage}">
                <div class="mb-5 flex items-center gap-2 rounded-lg bg-emerald-50 border border-emerald-200 px-4 py-3 text-sm text-emerald-700">
                    <i class="bi bi-check-circle-fill"></i>
                    <span><c:out value="${flashMessage}"/></span>
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/login" class="space-y-5">
                <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                <input type="hidden" name="next" value="<c:out value='${param.next}'/>">

                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1.5" for="email">Email address</label>
                    <div class="relative">
                        <i class="bi bi-envelope absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"></i>
                        <input type="email" id="email" name="email" maxlength="150"
                               value="<c:out value='${param.email}'/>"
                               required autofocus autocomplete="email"
                               placeholder="you@example.com"
                               class="w-full rounded-xl border border-slate-200 bg-slate-50 py-3 pl-11 pr-4 text-sm
                                      focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 outline-none transition">
                    </div>
                </div>

                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1.5" for="password">Password</label>
                    <div class="relative">
                        <i class="bi bi-lock absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"></i>
                        <input type="password" id="password" name="password" maxlength="100"
                               required autocomplete="current-password" placeholder="••••••••"
                               class="w-full rounded-xl border border-slate-200 bg-slate-50 py-3 pl-11 pr-12 text-sm
                                      focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 outline-none transition">
                        <button type="button" id="togglePwd" tabindex="-1"
                                class="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600">
                            <i class="bi bi-eye" id="togglePwdIcon"></i>
                        </button>
                    </div>
                </div>

                <button type="submit"
                        class="w-full flex items-center justify-center gap-2 rounded-xl bg-indigo-600 hover:bg-indigo-700
                               text-white font-semibold py-3 text-sm shadow-sm transition">
                    <i class="bi bi-box-arrow-in-right"></i>
                    Sign in
                </button>
            </form>

            <p class="mt-6 text-center text-sm text-slate-500">
                New resident?
                <a href="${pageContext.request.contextPath}/signup" class="font-semibold text-indigo-600 hover:text-indigo-700">
                    Create an account
                </a>
            </p>
        </div>
    </div>

    <p class="mt-5 text-center text-xs text-slate-400">
        <i class="bi bi-shield-lock mr-1"></i>Protected by CSRF &amp; PBKDF2 authentication
    </p>
</div>

<script src="${pageContext.request.contextPath}/assets/login.js"></script>
</body>
</html>