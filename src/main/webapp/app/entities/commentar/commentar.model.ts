import { BaseEntity } from './../../shared';

export class Commentar implements BaseEntity {
    constructor(
        public id?: number,
        public text?: string,
        public creationDate?: any,
        public creatorId?: number,
    ) {
    }
}
