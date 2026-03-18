from django.shortcuts import render, redirect
from django.views.generic import View
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm
from django.contrib.auth import login as auth_login, logout, authenticate
from django.contrib import messages
from .forms import FormLogin


def login_view(request):

    if request.method == 'POST':
        miForm = FormLogin(request.POST)

        if miForm.is_valid():
            datosForm = miForm.cleaned_data

            user = authenticate(
                request,
                username=datosForm["Cedula"],
                password=datosForm["contraseña"]
            )

            if user is not None:
                auth_login(request, user)
                return redirect('index')
            else:
                messages.error(request, "Credenciales incorrectas")
    else:
        miForm = FormLogin()

    return render(request, "login.html", {"miForm": miForm})