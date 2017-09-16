import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {Initiative} from './initiative.model';
import {ResponseWrapper, createRequestOption} from '../../shared';

@Injectable()
export class InitiativeService {

    private resourceUrl = SERVER_API_URL + 'api/initiatives';
    private similarInitUrls = this.resourceUrl + '/similar-title';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }

    create(initiative: Initiative): Observable<Initiative> {
        const copy = this.convert(initiative);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    findSimilarOld(title: String): Observable<Initiative[]> {
        return this.http.get(`${this.similarInitUrls}/${title}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    findSimilarNew(title: String): Observable<Initiative[]> {
        return this.http.get(`${this.similarInitUrls}/${title}` + '?status=PROPOSAL').map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(initiative: Initiative): Observable<Initiative> {
        const copy = this.convert(initiative);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Initiative> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
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

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
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

    private convert(initiative: Initiative): Initiative {
        const copy: Initiative = Object.assign({}, initiative);

        copy.creationDate = this.dateUtils.toDate(initiative.creationDate);
        return copy;
    }
}
