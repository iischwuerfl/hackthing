import {Component, Input, OnChanges, OnInit, SimpleChange} from '@angular/core';
import {News} from './news.model';

@Component({
    selector: 'jhi-news',
    templateUrl: './news.component.html',
    styleUrls: ['news.scss']
})
export class NewsComponent implements OnInit, OnChanges {
    @Input()
    news: News[];

    newsToShow: News[];

    ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
        let log: string[] = [];
        for (let propName in changes) {
            this.newsToShow = changes[propName].currentValue;
        }
    }

    constructor() {
    }

    ngOnInit() {
    }
}
