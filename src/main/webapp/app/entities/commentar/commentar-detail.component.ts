import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Commentar } from './commentar.model';
import { CommentarService } from './commentar.service';

@Component({
    selector: 'jhi-commentar-detail',
    templateUrl: './commentar-detail.component.html'
})
export class CommentarDetailComponent implements OnInit, OnDestroy {

    commentar: Commentar;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private commentarService: CommentarService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCommentars();
    }

    load(id) {
        this.commentarService.find(id).subscribe((commentar) => {
            this.commentar = commentar;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCommentars() {
        this.eventSubscriber = this.eventManager.subscribe(
            'commentarListModification',
            (response) => this.load(this.commentar.id)
        );
    }
}
