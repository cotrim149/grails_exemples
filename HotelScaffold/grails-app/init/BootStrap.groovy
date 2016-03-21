import hotelscaffold.Hotel

class BootStrap {

    def init = { servletContext ->
      new Hotel(name:"Marriot").save()
      new Hotel(name:"Sheraton").save()
    }
    def destroy = {
      Hotel.findByName("Marriot").delete()
      Hotel.findByName("Sheraton").delete()
    }
}
