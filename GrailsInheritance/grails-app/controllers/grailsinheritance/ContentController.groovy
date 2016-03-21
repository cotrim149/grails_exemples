package grailsinheritance

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ContentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Content.list(params), model:[contentCount: Content.count()]
    }

    def show(Content content) {
        respond content
    }

    def create() {
        respond new Content(params)
    }

    @Transactional
    def save(Content content) {
        if (content == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (content.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond content.errors, view:'create'
            return
        }

        content.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'content.label', default: 'Content'), content.id])
                redirect content
            }
            '*' { respond content, [status: CREATED] }
        }
    }

    def edit(Content content) {
        respond content
    }

    @Transactional
    def update(Content content) {
        if (content == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (content.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond content.errors, view:'edit'
            return
        }

        content.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'content.label', default: 'Content'), content.id])
                redirect content
            }
            '*'{ respond content, [status: OK] }
        }
    }

    @Transactional
    def delete(Content content) {

        if (content == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        content.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'content.label', default: 'Content'), content.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'content.label', default: 'Content'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
