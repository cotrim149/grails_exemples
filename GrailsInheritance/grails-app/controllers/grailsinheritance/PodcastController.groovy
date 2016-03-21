package grailsinheritance

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PodcastController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Podcast.list(params), model:[podcastCount: Podcast.count()]
    }

    def show(Podcast podcast) {
        respond podcast
    }

    def create() {
        respond new Podcast(params)
    }

    @Transactional
    def save(Podcast podcast) {
        if (podcast == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (podcast.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond podcast.errors, view:'create'
            return
        }

        podcast.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'podcast.label', default: 'Podcast'), podcast.id])
                redirect podcast
            }
            '*' { respond podcast, [status: CREATED] }
        }
    }

    def edit(Podcast podcast) {
        respond podcast
    }

    @Transactional
    def update(Podcast podcast) {
        if (podcast == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (podcast.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond podcast.errors, view:'edit'
            return
        }

        podcast.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'podcast.label', default: 'Podcast'), podcast.id])
                redirect podcast
            }
            '*'{ respond podcast, [status: OK] }
        }
    }

    @Transactional
    def delete(Podcast podcast) {

        if (podcast == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        podcast.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'podcast.label', default: 'Podcast'), podcast.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'podcast.label', default: 'Podcast'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
