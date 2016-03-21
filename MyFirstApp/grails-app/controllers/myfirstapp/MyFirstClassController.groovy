package myfirstapp

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MyFirstClassController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond MyFirstClass.list(params), model:[myFirstClassCount: MyFirstClass.count()]
    }

    def show(MyFirstClass myFirstClass) {
        respond myFirstClass
    }

    def create() {
        respond new MyFirstClass(params)
    }

    @Transactional
    def save(MyFirstClass myFirstClass) {
        if (myFirstClass == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (myFirstClass.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond myFirstClass.errors, view:'create'
            return
        }

        myFirstClass.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'myFirstClass.label', default: 'MyFirstClass'), myFirstClass.id])
                redirect myFirstClass
            }
            '*' { respond myFirstClass, [status: CREATED] }
        }
    }

    def edit(MyFirstClass myFirstClass) {
        respond myFirstClass
    }

    @Transactional
    def update(MyFirstClass myFirstClass) {
        if (myFirstClass == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (myFirstClass.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond myFirstClass.errors, view:'edit'
            return
        }

        myFirstClass.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'myFirstClass.label', default: 'MyFirstClass'), myFirstClass.id])
                redirect myFirstClass
            }
            '*'{ respond myFirstClass, [status: OK] }
        }
    }

    @Transactional
    def delete(MyFirstClass myFirstClass) {

        if (myFirstClass == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        myFirstClass.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'myFirstClass.label', default: 'MyFirstClass'), myFirstClass.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'myFirstClass.label', default: 'MyFirstClass'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
