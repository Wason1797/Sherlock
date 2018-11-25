from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
import pyscreenshot

# Create your views here.


class CaptureView(APIView):

    def get(self, request):
      im = pyscreenshot.grab(bbox=(200, 200, 1000, 1000))

      im.save('img.png')
      return Response("{status:Capture_Complete}")
