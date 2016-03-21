package extraclass2

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CourseControllerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond CourseController.list(params), model:[courseControllerCount: CourseController.count()]
    }

    def show(CourseController courseController) {
        respond courseController
    }

    def create() {
        respond new CourseController(params)
    }

    @Transactional
    def save(CourseController courseController) {
        if (courseController == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (courseController.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond courseController.errors, view:'create'
            return
        }

        courseController.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'courseController.label', default: 'CourseController'), courseController.id])
                redirect courseController
            }
            '*' { respond courseController, [status: CREATED] }
        }
    }

    def edit(CourseController courseController) {
        respond courseController
    }

    @Transactional
    def update(CourseController courseController) {
        if (courseController == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (courseController.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond courseController.errors, view:'edit'
            return
        }

        courseController.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'courseController.label', default: 'CourseController'), courseController.id])
                redirect courseController
            }
            '*'{ respond courseController, [status: OK] }
        }
    }

    @Transactional
    def delete(CourseController courseController) {

        if (courseController == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        courseController.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'courseController.label', default: 'CourseController'), courseController.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'courseController.label', default: 'CourseController'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
