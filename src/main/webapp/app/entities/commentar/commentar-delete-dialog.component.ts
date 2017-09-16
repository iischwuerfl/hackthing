import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Commentar } from './commentar.model';
import { CommentarPopupService } from './commentar-popup.service';
import { CommentarService } from './commentar.service';

@Component({
    selector: 'jhi-commentar-delete-dialog',
    templateUrl: './commentar-delete-dialog.component.html'
})
export class CommentarDeleteDialogComponent {

    commentar: Commentar;

    constructor(
        private commentarService: CommentarService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.commentarService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'commentarListModification',
                content: 'Deleted an commentar'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-commentar-delete-popup',
    template: ''
})
export class CommentarDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private commentarPopupService: CommentarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.commentarPopupService
                .open(CommentarDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
