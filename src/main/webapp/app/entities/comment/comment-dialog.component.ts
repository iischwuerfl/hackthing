import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Comment} from './comment.model';
import {CommentPopupService} from './comment-popup.service';
import {CommentService} from './comment.service';
import {Initiative, InitiativeService} from '../initiative';
import {User, UserService} from '../../shared';
import {ResponseWrapper, Principal} from '../../shared';

@Component({
    selector: 'jhi-comment-dialog',
    templateUrl: './comment-dialog.component.html'
})
export class CommentDialogComponent implements OnInit {

    comment: Comment;
    isSaving: boolean;

    initiativeId: number;

    initiatives: Initiative[];

    account: Account;
    users: User[];

    constructor(public activeModal: NgbActiveModal,
                private alertService: JhiAlertService,
                private commentService: CommentService,
                private initiativeService: InitiativeService,
                private userService: UserService,
                private eventManager: JhiEventManager,
                private route: ActivatedRoute,
                private principal: Principal) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.initiativeService.query()
            .subscribe((res: ResponseWrapper) => {
                this.initiatives = res.json;
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => {
                this.users = res.json;
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.route.params.subscribe((params) => {
            console.log(params);
            this.initiativeId = params['initiativeId'];
        });
        this.principal.identity().then((account) => {
            this.account = account;
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.comment.creatorId = parseInt(this.account.id, 10);
        console.log('comment to save', this.comment);
        if (this.comment.id !== null) {
            this.subscribeToSaveResponse(
                this.commentService.update(this.comment));
        } else {
            this.subscribeToSaveResponse(
                this.commentService.create(this.comment));
        }
    }

    private subscribeToSaveResponse(result: Observable<Comment>) {
        result.subscribe((res: Comment) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Comment) {
        this.eventManager.broadcast({name: 'commentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackInitiativeById(index: number, item: Initiative) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-comment-popup',
    template: ''
})
export class CommentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private commentPopupService: CommentPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.commentPopupService
                    .open(CommentDialogComponent as Component, params['id'], false);
            } else if (params['initiativeID']) {
                this.commentPopupService
                    .open(CommentDialogComponent as Component, params['initiativeID'], true);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
