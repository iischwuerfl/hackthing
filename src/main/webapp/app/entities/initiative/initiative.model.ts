import { BaseEntity, User } from './../../shared';
import {Comment} from "../comment/comment.model";

export const enum Status {
    'PROPOSAL',
    'COLLECTING',
    'VOTING',
    'FINISHED'
}

export class Initiative implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public text?: string,
        public status?: Status,
        public creationDate?: any,
        public comments?: Comment[],
        public initiatorId?: number,
        public citizenSupporters?: User[],
        public politicianSupporters?: User[],
    ) {
    }
}
