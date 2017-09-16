import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {Politician} from './politician.model';
import {ResponseWrapper, createRequestOption} from '../../shared';
import {NewsFeed} from '../news/newsfeed.model';

@Injectable()
export class PoliticianService {

    private resourceUrl = SERVER_API_URL + 'api/politician';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }

    find(name: string): Observable<NewsFeed> {
        return this.http.get(this.resourceUrl + '?query=' + name).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.creationDate = this.dateUtils
            .convertDateTimeFromServer(entity.creationDate);
    }

    private convert(politician: Politician): Politician {
        const copy: Politician = Object.assign({}, politician);
        return copy;
    }
}
