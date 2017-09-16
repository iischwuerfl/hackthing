import {Component, Input, OnChanges, OnInit, SimpleChange} from '@angular/core';
import {News} from './news.model';

@Component({
    selector: 'jhi-news',
    templateUrl: './news.component.html',
    styleUrls: ['news.scss']
})
export class NewsComponent implements OnChanges {
    @Input()
    news: News[];

    newsToShow: News[];

    ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
        for (const propName in changes) {
            if (propName) {
                this.newsToShow = changes[propName].currentValue;
            }
        }
    }

    toggleDescription(news: any) {
        news.showDetails = !news.showDetails;
    }

    constructor() {
    }
}
