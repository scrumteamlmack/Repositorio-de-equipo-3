from django.shortcuts import render, redirect
from Prueba import settings
from django.contrib import messages
from django.contrib.auth import authenticate, login, logout
from django.views.generic import View
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm

# Create your views here.

def login(request):
 if request.method == 'POST':
        doc = request.POST.get('doc')
        password = request.POST.get('pass')

        user = authenticate(request, username=doc, password=password)

        if user is not None:
            login(request, user)
            return redirect('index')
        else:
            messages.error(request, 'Credenciales incorrectas')
            
        return render(request, 'login.html')
