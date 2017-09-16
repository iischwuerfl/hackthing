import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Commentar } from './commentar.model';
import { CommentarPopupService } from './commentar-popup.service';
import { CommentarService } from './commentar.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-commentar-dialog',
    templateUrl: './commentar-dialog.component.html'
})
export class CommentarDialogComponent implements OnInit {

    commentar: Commentar;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private commentarService: CommentarService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.commentar.id !== undefined) {
            this.subscribeToSaveResponse(
                this.commentarService.update(this.commentar));
        } else {
            this.subscribeToSaveResponse(
                this.commentarService.create(this.commentar));
        }
    }

    private subscribeToSaveResponse(result: Observable<Commentar>) {
        result.subscribe((res: Commentar) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Commentar) {
        this.eventManager.broadcast({ name: 'commentarListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-commentar-popup',
    template: ''
})
export class CommentarPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private commentarPopupService: CommentarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.commentarPopupService
                    .open(CommentarDialogComponent as Component, params['id']);
            } else {
                this.commentarPopupService
                    .open(CommentarDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
