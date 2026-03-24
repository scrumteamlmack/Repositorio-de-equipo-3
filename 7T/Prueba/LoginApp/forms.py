from django import forms

class FormLogin(forms.Form):
    Cedula = forms.IntegerField(error_messages={"invalid" : "El numero de documento no es valido"})
    contraseña = forms.CharField(label="contraseña", widget=forms.PasswordInput, error_messages={"invalid" : "La contraseña es invalida "})