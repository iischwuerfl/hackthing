import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {Initiative} from './initiative.model';
import {InitiativeService} from './initiative.service';

@Component({
    selector: 'jhi-initiative-detail',
    templateUrl: './initiative-detail.component.html',
    styleUrls: [
        './initiative.scss'
    ]
})
export class InitiativeDetailComponent implements OnInit, OnDestroy {

    initiative: Initiative;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager,
                private initiativeService: InitiativeService,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInInitiatives();
    }

    load(id) {
        this.initiativeService.find(id).subscribe((initiative) => {
            this.initiative = initiative;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInInitiatives() {
        this.eventSubscriber = this.eventManager.subscribe(
            'initiativeListModification',
            (response) => this.load(this.initiative.id)
        );
    }

    getRandomLikeNumber() {
        return Math.floor((Math.random() * 20) + 1);
    }
}
