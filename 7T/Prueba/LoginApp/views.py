from django.shortcuts import render, redirect
#from django.views.generic import View
#from django.contrib.auth.forms import UserCreationForm, AuthenticationForm
#from django.contrib.auth import login, logout, authenticate
#from django.contrib import messages
# Create your views here.

def login(request):
  return render(request, 'login.html')
 #if request.method == 'POST':
  #      doc = request.POST.get('doc')
   #     password = request.POST.get('pass')
#
 #       user = authenticate(request, username=doc, password=password)
#
 #       if user is not None:
  #          login(request, user)
   #         return redirect('index')
    #  else:
     #       messages.error(request, 'Credenciales incorrectas')
      #      
       
