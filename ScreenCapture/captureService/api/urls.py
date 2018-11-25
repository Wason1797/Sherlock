from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from api import views
from django.urls import path

urlpatterns = [   
    path('screenshot/get/', views.CaptureView.as_view()),    
    
]