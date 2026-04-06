from django.urls import path
from . import views

app_name = 'mackapp'

urlpatterns = [
    path('', views.index, name="index")

]
