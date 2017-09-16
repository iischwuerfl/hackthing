import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Initiative} from './initiative.model';
import {InitiativeService} from './initiative.service';
import {User, UserService} from '../../shared';
import {ResponseWrapper} from '../../shared';
import {DatePipe} from '@angular/common';
import {FormControl} from '@angular/forms';

@Component({
    selector: 'jhi-initiative',
    templateUrl: './initiative-create-edit.component.html'
})
export class InitiativeCreateEditComponent implements OnInit {

    initiative: Initiative;
    isSaving: boolean;
    routeSub: any;
    users: User[];
    similarInitiativesOld: Initiative[];
    similarInitiativesNew: Initiative[];
    title: any = new FormControl();

    constructor(private alertService: JhiAlertService,
                private initiativeService: InitiativeService,
                private userService: UserService,
                private eventManager: JhiEventManager,
                private route: ActivatedRoute,
                private datePipe: DatePipe,
                private router: Router) {

        this.title.valueChanges
            .debounceTime(400)
            .distinctUntilChanged()
            .subscribe((title) => {
                if (title !== '') {
                    this.initiativeService.findSimilarNew(title).subscribe((res: Initiative[]) => {
                        this.similarInitiativesNew = res;
                        console.log('similarInitiativesNew', this.similarInitiativesNew);
                    });
                    this.initiativeService.findSimilarOld(title).subscribe((res: Initiative[]) => {
                        this.similarInitiativesOld = res;
                        console.log('similarInitiativesOld', this.similarInitiativesOld);
                    });
                }
            });
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => {
                this.users = res.json;
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.routeSub = this.route.params.subscribe((params) => {

            const id = params['id'];
            if (id) {
                this.initiativeService.find(id).subscribe((initiative) => {
                    initiative.creationDate = this.datePipe
                        .transform(initiative.creationDate, 'yyyy-MM-ddTHH:mm:ss');
                    // this.ngbModalRef = this.initiativeModalRef(component, initiative);
                    // resolve(this.ngbModalRef);
                });
            } else {
                this.initiative = new Initiative();
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    // this.ngbModalRef = this.initiativeModalRef(component, new Initiative());
                    // resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    save() {
        this.isSaving = true;
        this.initiative.title = this.title._value;
        if (this.initiative.id !== undefined) {
            this.subscribeToSaveResponse(
                this.initiativeService.update(this.initiative));
        } else {
            this.subscribeToSaveResponse(
                this.initiativeService.create(this.initiative));
        }
    }

    private subscribeToSaveResponse(result: Observable<Initiative>) {
        result.subscribe((res: Initiative) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Initiative) {
        this.eventManager.broadcast({name: 'initiativeListModification', content: 'OK'});
        this.router.navigate(['/initiative']);
        this.isSaving = false;
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
