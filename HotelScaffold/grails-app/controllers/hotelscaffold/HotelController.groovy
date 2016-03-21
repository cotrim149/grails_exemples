package hotelscaffold

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class HotelController {
    static scaffold = Hotel
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Hotel.list(params), model:[hotelCount: Hotel.count()]
    }

    def show(Hotel hotel) {
        respond hotel
    }

    def create() {
        respond new Hotel(params)
    }

    @Transactional
    def save(Hotel hotel) {
        if (hotel == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (hotel.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond hotel.errors, view:'create'
            return
        }

        hotel.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'hotel.label', default: 'Hotel'), hotel.id])
                redirect hotel
            }
            '*' { respond hotel, [status: CREATED] }
        }
    }

    def edit(Hotel hotel) {
        respond hotel
    }

    @Transactional
    def update(Hotel hotel) {
        if (hotel == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (hotel.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond hotel.errors, view:'edit'
            return
        }

        hotel.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'hotel.label', default: 'Hotel'), hotel.id])
                redirect hotel
            }
            '*'{ respond hotel, [status: OK] }
        }
    }

    @Transactional
    def delete(Hotel hotel) {

        if (hotel == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        hotel.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'hotel.label', default: 'Hotel'), hotel.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'hotel.label', default: 'Hotel'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
