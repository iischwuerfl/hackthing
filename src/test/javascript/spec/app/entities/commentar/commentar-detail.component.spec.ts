/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { MeineInitiativeTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CommentarDetailComponent } from '../../../../../../main/webapp/app/entities/commentar/commentar-detail.component';
import { CommentarService } from '../../../../../../main/webapp/app/entities/commentar/commentar.service';
import { Commentar } from '../../../../../../main/webapp/app/entities/commentar/commentar.model';

describe('Component Tests', () => {

    describe('Commentar Management Detail Component', () => {
        let comp: CommentarDetailComponent;
        let fixture: ComponentFixture<CommentarDetailComponent>;
        let service: CommentarService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MeineInitiativeTestModule],
                declarations: [CommentarDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CommentarService,
                    JhiEventManager
                ]
            }).overrideTemplate(CommentarDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CommentarDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CommentarService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Commentar(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.commentar).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
