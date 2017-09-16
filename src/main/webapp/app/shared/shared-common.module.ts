import { NgModule, LOCALE_ID } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {
    MeineInitiativeSharedLibsModule,
    JhiAlertComponent,
    JhiAlertErrorComponent
} from './';
import {NewsComponent} from '../entities/news/news.component';

@NgModule({
    imports: [
        MeineInitiativeSharedLibsModule
    ],
    declarations: [
        JhiAlertComponent,
        JhiAlertErrorComponent,
        NewsComponent
    ],
    providers: [
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'en'
        },
    ],
    exports: [
        MeineInitiativeSharedLibsModule,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        NewsComponent
    ]
})
export class MeineInitiativeSharedCommonModule {}
