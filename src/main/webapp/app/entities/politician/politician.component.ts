import {Component, OnInit} from '@angular/core';
import {PoliticianService} from './politician.service';
import {ActivatedRoute} from '@angular/router';
import {Politician} from './politician.model';
import {NewsFeed} from '../news/newsfeed.model';

@Component({
    selector: 'jhi-politician',
    templateUrl: './politician.component.html',
    styleUrls: ['politician.scss']
})
export class PoliticianComponent implements OnInit {

    private politician: Politician;

    public firstName: string;
    public lastName: string;
    public newsFeed: NewsFeed = new NewsFeed();

    constructor(private politicianService: PoliticianService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.params.subscribe((params) => {
            const name = params['id'];
            this.load(name);
            const nameSplit = name.split('\+');
            this.firstName = nameSplit[0].charAt(0).toUpperCase() + nameSplit[0].slice(1);
            this.lastName = nameSplit[1].charAt(0).toUpperCase() + nameSplit[1].slice(1);

        });
    }

    load(id) {
        const nameSplit = id.split('\+');
        const lastName = nameSplit[1].charAt(0).toUpperCase() + nameSplit[1].slice(1);
        this.politicianService.find(lastName).subscribe((newsFeed) => {
            this.newsFeed = newsFeed;
        });
    }
}
