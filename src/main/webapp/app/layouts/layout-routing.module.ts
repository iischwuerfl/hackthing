import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { appRoutes } from '../app.route';
import { errorRoute } from './';

const LAYOUT_ROUTES = [
        ...appRoutes,
    ...errorRoute
];

@NgModule({
    imports: [
        RouterModule.forRoot(LAYOUT_ROUTES, { useHash: true })
    ],
    exports: [
        RouterModule
    ]
})
export class LayoutRoutingModule {}
