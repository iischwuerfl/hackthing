/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { MeineInitiativeTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { InitiativeDetailComponent } from '../../../../../../main/webapp/app/entities/initiative/initiative-detail.component';
import { InitiativeService } from '../../../../../../main/webapp/app/entities/initiative/initiative.service';
import { Initiative } from '../../../../../../main/webapp/app/entities/initiative/initiative.model';

describe('Component Tests', () => {

    describe('Initiative Management Detail Component', () => {
        let comp: InitiativeDetailComponent;
        let fixture: ComponentFixture<InitiativeDetailComponent>;
        let service: InitiativeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MeineInitiativeTestModule],
                declarations: [InitiativeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    InitiativeService,
                    JhiEventManager
                ]
            }).overrideTemplate(InitiativeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InitiativeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InitiativeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Initiative(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.initiative).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
