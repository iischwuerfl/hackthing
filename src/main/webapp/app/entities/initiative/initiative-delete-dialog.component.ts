import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Initiative } from './initiative.model';
import { InitiativePopupService } from './initiative-popup.service';
import { InitiativeService } from './initiative.service';

@Component({
    selector: 'jhi-initiative-delete-dialog',
    templateUrl: './initiative-delete-dialog.component.html'
})
export class InitiativeDeleteDialogComponent {

    initiative: Initiative;

    constructor(
        private initiativeService: InitiativeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.initiativeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'initiativeListModification',
                content: 'Deleted an initiative'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-initiative-delete-popup',
    template: ''
})
export class InitiativeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private initiativePopupService: InitiativePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.initiativePopupService
                .open(InitiativeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
