import {Routes} from '@angular/router';

import {NavbarComponent} from './layouts';

export const appRoutes: Routes =
    [
        {path: '', component: NavbarComponent, outlet: 'navbar'}
    ];
