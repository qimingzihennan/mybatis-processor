package {{metadata.packageName}};

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.LinkedHashMap;

import {{metadata.domainClazzName}};
import {{metadata.repositoryClazzName}};
import com.vcg.mybatis.example.processor.MybatisExampleRepository;

public class {{metadata.exampleClazzSimpleName}} implements Serializable {

    private static final long serialVersionUID = 314035125506252121L;

    {{#metadata.primaryMetadata}}
    private static MybatisExampleRepository<{{metadata.domainClazzSimpleName}},{{javaType}},{{metadata.exampleClazzSimpleName}}> CRUD_REPOSITORY;
    {{/metadata.primaryMetadata}}

    {{^metadata.primaryMetadata}}
    private static MybatisExampleRepository<{{metadata.domainClazzSimpleName}},Object,{{metadata.exampleClazzSimpleName}}> CRUD_REPOSITORY;
    {{/metadata.primaryMetadata}}

    public static final String TABLE_NAME = "{{metadata.tableName}}";

    {{#metadata.columnMetadataList}}
    public static final String {{fieldName}} = "{{columnName}}";

    {{/metadata.columnMetadataList}}
    private List<String> columns;

    private List<Integer> limit;

    private String orderByClause;

    private boolean distinct;

    private List<Criteria> oredCriteria;

    private Criteria currentCriteria;

    private Integer page;

    private Integer size;

    private static final String DESC = " DESC";

    private static final String ASC = " ASC";

    private String table;

    {{#metadata.partitionKey}}
    private static final String partitionKey = "{{fieldName}}";

    private static final Integer shard = {{metadata.shard}};

    private static final List<String> shardTables = getShardTables();

    {{/metadata.partitionKey}}
    private {{metadata.domainClazzSimpleName}} record;

    private List<{{metadata.domainClazzSimpleName}}> records;

    public {{metadata.exampleClazzSimpleName}}() {}

    {{#metadata.primaryMetadata}}
    private {{metadata.exampleClazzSimpleName}}(MybatisExampleRepository<{{metadata.domainClazzSimpleName}},{{javaType}},{{metadata.exampleClazzSimpleName}}> repository) {
        if(CRUD_REPOSITORY == null){
            CRUD_REPOSITORY = repository;
        }
    }

    public static {{metadata.exampleClazzSimpleName}} repository(MybatisExampleRepository<{{metadata.domainClazzSimpleName}},{{javaType}},{{metadata.exampleClazzSimpleName}}> repository){
        return new {{metadata.exampleClazzSimpleName}}(repository);
    }
    {{/metadata.primaryMetadata}}


    {{^metadata.primaryMetadata}}
    private {{metadata.exampleClazzSimpleName}}(MybatisExampleRepository<{{metadata.repositoryClazzSimpleName}},Object,{{metadata.exampleClazzSimpleName}}> repository) {
        if(CRUD_REPOSITORY == null){
            CRUD_REPOSITORY = repository;
        }
    }

    public static {{metadata.exampleClazzSimpleName}} repository(MybatisExampleRepository<{{metadata.repositoryClazzSimpleName}},Object,{{metadata.exampleClazzSimpleName}}> repository){
        return new {{metadata.exampleClazzSimpleName}}(repository);
    }
    {{/metadata.primaryMetadata}}



    public {{metadata.exampleClazzSimpleName}} limit(Integer... limit) {
        this.limit = Arrays.asList(limit);
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} columns(String... columns) {
        columns(Arrays.asList(columns));
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} columns(List<String> columns) {
        if(this.columns != null) {
            this.columns.addAll(columns);
        } else {
            this.columns = columns;
        }
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} ignoreColumns(String... columns) {
        ignoreColumns(Arrays.asList(columns));
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} ignoreColumns(List<String> columns) {
        if(this.columns==null || this.columns.isEmpty()){
            this.columns = allColumns();
        }
        this.columns.removeAll(columns);
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} orderByClause(String orderByClause) {
        setOrderByClause(orderByClause);
        return this;
    }

    private {{metadata.exampleClazzSimpleName}} criteria(Criteria criteria) {
        getOredCriteria().add(criteria);
        this.currentCriteria = criteria;
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} distinct(boolean distinct) {
        setDistinct(distinct);
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} page(Integer page, Integer size) {
        if(page==null || size==null || page<=0 ||size <=0){
            throw new RuntimeException("page or size for condition cannot be null or less 1");
        }
        this.page = page;
        this.size = size;
        this.limit = Arrays.asList((page - 1) * size, size);
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} asc(String... columns){
        String orderBy = String.join(",",columns) + ASC;
        orderByClause(this.orderByClause==null ? orderBy : (this.orderByClause +","+ orderBy));
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} desc(String... columns){
        String orderBy = String.join(",",columns) + DESC;
        orderByClause(this.orderByClause==null ? orderBy : (this.orderByClause +","+ orderBy));
        return this;
    }

    public List<{{metadata.domainClazzSimpleName}}> get(){
        {{#metadata.partitionKey}}
        checkTable();
        {{/metadata.partitionKey}}
        return CRUD_REPOSITORY.selectByExample(this);
    }

    public {{metadata.domainClazzSimpleName}} getOne(){
        {{#metadata.partitionKey}}
        checkTable();
        {{/metadata.partitionKey}}
        limit(1);
        List<{{metadata.domainClazzSimpleName}}> result = CRUD_REPOSITORY.selectByExample(this);
        return result.isEmpty() ? null : result.get(0);
    }

    @SuppressWarnings("unchecked")
    public  <V> List<V> get(String column, Class<V> v){
        {{#metadata.partitionKey}}
        checkTable();
        {{/metadata.partitionKey}}
        this.columns = Arrays.asList(column);
        List<Map<String,Object>> result = CRUD_REPOSITORY.selectByExampleWithMap(this);
        List<V> values = new ArrayList<>(result.size());
        for(Map<String, Object> map : result){
           V value = (V) map.get(column);
           values.add(value);
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    public  <V> V getOne(String column, Class<V> v){
        {{#metadata.partitionKey}}
        checkTable();
        {{/metadata.partitionKey}}
        limit(1);
        this.columns = Arrays.asList(column);
        List<Map<String,Object>> result = CRUD_REPOSITORY.selectByExampleWithMap(this);
        return result.isEmpty() ? null : (V) result.get(0).get(column);
    }

    public com.vcg.mybatis.example.processor.domain.PageInfo<{{metadata.domainClazzSimpleName}}> getPage(){
        {{#metadata.partitionKey}}
        checkTable();
        {{/metadata.partitionKey}}
        if(this.page==null || this.size==null){
            throw new RuntimeException("page or size for condition cannot be null");
        }
        return new com.vcg.mybatis.example.processor.domain.PageInfo<>(this.page, this.size, count(), get());
    }

    public long count(){
        {{#metadata.partitionKey}}
        checkTable();
        {{/metadata.partitionKey}}
        return CRUD_REPOSITORY.countByExample(this);
    }

    public boolean exist(){
        {{#metadata.partitionKey}}
        checkTable();
        {{/metadata.partitionKey}}
        checkCriteria();
        Boolean exist = CRUD_REPOSITORY.existByExample(this);
        return exist!=null&&exist;
    }

    public int update({{metadata.domainClazzSimpleName}} t){
        {{#metadata.partitionKey}}
        checkTable();
        {{/metadata.partitionKey}}
        checkCriteria();
        return CRUD_REPOSITORY.updateByExampleSelective(t,this);
    }

    public {{metadata.domainClazzSimpleName}} insert({{metadata.domainClazzSimpleName}} t){
        {{#metadata.partitionKey}}
        shardTable(t.get{{firstUpFieldName}}());
        this.record = t;
        CRUD_REPOSITORY.insertByExample(this);
        this.record = null;
        {{/metadata.partitionKey}}
        {{^metadata.partitionKey}}
        CRUD_REPOSITORY.insert(t);
        {{/metadata.partitionKey}}
        return t;
    }

    public {{metadata.domainClazzSimpleName}} insertSelective({{metadata.domainClazzSimpleName}} t){
        {{#metadata.partitionKey}}
        shardTable(t.get{{firstUpFieldName}}());
        this.record = t;
        CRUD_REPOSITORY.insertSelectiveByExample(this);
        this.record = null;
        {{/metadata.partitionKey}}
        {{^metadata.partitionKey}}
        CRUD_REPOSITORY.insertSelective(t);
        {{/metadata.partitionKey}}
        return t;
    }

    public List<{{metadata.domainClazzSimpleName}}> insert(List<{{metadata.domainClazzSimpleName}}> ts){
        {{#metadata.partitionKey}}
        Map<Integer, List<Comment>> batchRecordsMap = new LinkedHashMap<>();
        for ({{metadata.domainClazzSimpleName}} t : ts) {
            {{#stringType}}
            int partitionValue = com.vcg.mybatis.example.processor.util.Crc16Utils.getSlot(t.get{{firstUpFieldName}}()) % shard;
            {{/stringType}}
            {{^stringType}}
            {{javaType}} partitionValue = t.get{{firstUpFieldName}}();
            {{/stringType}}

            List<{{metadata.domainClazzSimpleName}}> records = batchRecordsMap.get(partitionValue);
            if (records == null) {
                records = new ArrayList<>();
                batchRecordsMap.put(partitionValue,records);

            }
            records.add(t);
        }

        for (Map.Entry<Integer, List<{{metadata.domainClazzSimpleName}}>> entry : batchRecordsMap.entrySet()) {
            shardTable(entry.getKey());
            this.records = entry.getValue();
            CRUD_REPOSITORY.insertBatchByExample(this);
        }
        this.records = ts;
        CRUD_REPOSITORY.insertBatchByExample(this);
        this.records = null;
        {{/metadata.partitionKey}}
        {{^metadata.partitionKey}}
        CRUD_REPOSITORY.insertBatch(ts);
        {{/metadata.partitionKey}}
        return ts;
    }

    public int delete(){
        checkCriteria();
        return CRUD_REPOSITORY.deleteByExample(this);
    }


    private void checkCriteria(){
        if(this.currentCriteria == null){
            throw new RuntimeException("criteria for condition cannot be null");
        }
    }
    private void checkTable(){
        {{#metadata.partitionKey}}
        if(table == null){
            throw new RuntimeException("{{fieldName}} must have a value");
        }
        {{/metadata.partitionKey}}
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    protected String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    protected List<Criteria> getOredCriteria() {
        if(this.oredCriteria == null){
            this.oredCriteria = new ArrayList<>();
        }
        return this.oredCriteria;
    }

    private {{metadata.exampleClazzSimpleName}} or(Criteria criteria) {
        getOredCriteria().add(criteria);
        this.currentCriteria = criteria;
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} or() {
        return or(createCriteriaInternal());
    }

    protected Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        List<Criteria> cs = getOredCriteria();
        if (cs.size() == 0) {
            cs.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        this.oredCriteria.clear();
        this.orderByClause = null;
        this.distinct = false;
        this.limit = null;
        this.columns = null;
        this.page = null;
        this.size = null;
        this.table = null;
        this.currentCriteria = null;
        this.table = null;
    }

{{#metadata.columnMetadataList}}
    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}IsNull() {
        getCriteria().and{{firstUpFieldName}}IsNull();
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}IsNotNull() {
        getCriteria().and{{firstUpFieldName}}IsNotNull();
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}EqualTo({{javaType}} {{fieldName}}) {
        {{#partitionKey}}
        shardTable({{fieldName}});
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}EqualTo({{fieldName}});
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}NotEqualTo({{javaType}} {{fieldName}}) {
        {{#partitionKey}}
        shardTable({{fieldName}});
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}NotEqualTo({{fieldName}});
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}In(List<{{javaType}}> {{fieldName}}) {
        {{#partitionKey}}
        if(!{{fieldName}}.isEmpty()){
            shardTable({{fieldName}}.get(0));
        }
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}In({{fieldName}});
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}NotIn(List<{{javaType}}> {{fieldName}}) {
        {{#partitionKey}}
        if(!{{fieldName}}.isEmpty()){
            shardTable({{fieldName}}.get(0));
        }
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}NotIn({{fieldName}});
        return this;
    }

    {{^stringType}}
    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}Between({{javaType}} {{fieldName}}1, {{javaType}} {{fieldName}}2) {
        {{#partitionKey}}
        shardTable({{fieldName}}1);
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}Between({{fieldName}}1, {{fieldName}}2);
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}NotBetween({{javaType}} {{fieldName}}1, {{javaType}} {{fieldName}}2) {
        {{#partitionKey}}
        shardTable({{fieldName}}1);
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}NotBetween({{fieldName}}1, {{fieldName}}2);
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}GreaterThan({{javaType}} {{fieldName}}) {
        {{#partitionKey}}
        shardTable({{fieldName}});
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}GreaterThan({{fieldName}});
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}GreaterThanOrEqualTo({{javaType}} {{fieldName}}) {
        {{#partitionKey}}
        shardTable({{fieldName}});
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}GreaterThanOrEqualTo({{fieldName}});
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}LessThan({{javaType}} {{fieldName}}) {
        {{#partitionKey}}
        shardTable({{fieldName}});
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}LessThan({{fieldName}});
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}LessThanOrEqualTo({{javaType}} {{fieldName}}) {
        {{#partitionKey}}
        shardTable({{fieldName}});
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}LessThanOrEqualTo({{fieldName}});
        return this;
    }
    {{/stringType}}

    {{#stringType}}
    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}Like({{javaType}} {{fieldName}}) {
        {{#partitionKey}}
        shardTable({{fieldName}});
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}Like({{fieldName}});
        return this;
    }

    public {{metadata.exampleClazzSimpleName}} and{{firstUpFieldName}}NotLike({{javaType}} {{fieldName}}) {
        {{#partitionKey}}
        shardTable({{fieldName}});
        {{/partitionKey}}
        getCriteria().and{{firstUpFieldName}}NotLike({{fieldName}});
        return this;
    }
    {{/stringType}}
{{/metadata.columnMetadataList}}

    {{#metadata.partitionKey}}
    protected void shardTable(Integer value){
        if(value==null){
            throw new RuntimeException("value for "+partitionKey+ "cannot be null");
        }
        this.table = shardTables.get(value.intValue() % this.shard);
    }

    protected void shardTable(Long value){
        if(value==null){
            throw new RuntimeException("value for "+partitionKey+" cannot be null");
        }
        this.table = shardTables.get((int)value.longValue() % this.shard);
    }

    protected void shardTable(String value){
        if(value==null){
            throw new RuntimeException("value for "+partitionKey+" cannot be null");
        }
        int slot = com.vcg.mybatis.example.processor.util.Crc16Utils.getSlot(value);
        this.table = shardTables.get(slot % this.shard);
    }
    {{/metadata.partitionKey}}


    private Criteria getCriteria(){
        if(this.currentCriteria == null){
            this.currentCriteria = new Criteria();
            getOredCriteria().add(this.currentCriteria);
        }
        return this.currentCriteria;
    }

    protected abstract static class GeneratedCriteria implements Serializable {

        private static final long serialVersionUID = 1543541368793026835L;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }
{{#metadata.columnMetadataList}}
        public Criteria and{{firstUpFieldName}}IsNull() {
            addCriterion("{{columnName}} is null");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}IsNotNull() {
            addCriterion("{{columnName}} is not null");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}EqualTo({{javaType}} {{fieldName}}) {
            addCriterion("{{columnName}} =", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}NotEqualTo({{javaType}} {{fieldName}}) {
            addCriterion("{{columnName}} <>", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}In(List<{{javaType}}> {{fieldName}}) {
            addCriterion("{{columnName}} in", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}NotIn(List<{{javaType}}> {{fieldName}}) {
            addCriterion("{{columnName}} not in", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }

        {{^stringType}}
        public Criteria and{{firstUpFieldName}}Between({{javaType}} {{fieldName}}1, {{javaType}} {{fieldName}}2) {
            addCriterion("{{columnName}} between", {{fieldName}}1, {{fieldName}}2, "{{fieldName}}");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}NotBetween({{javaType}} {{fieldName}}1, {{javaType}} {{fieldName}}2) {
            addCriterion("{{columnName}} not between", {{fieldName}}1, {{fieldName}}2, "{{fieldName}}");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}GreaterThan({{javaType}} {{fieldName}}) {
            addCriterion("{{columnName}} >", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}GreaterThanOrEqualTo({{javaType}} {{fieldName}}) {
            addCriterion("{{columnName}} >=", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}LessThan({{javaType}} {{fieldName}}) {
            addCriterion("{{columnName}} <", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}LessThanOrEqualTo({{javaType}} {{fieldName}}) {
            addCriterion("{{columnName}} <=", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }
        {{/stringType}}

        {{#stringType}}
        public Criteria and{{firstUpFieldName}}Like({{javaType}} {{fieldName}}) {
            addCriterion("{{columnName}} like", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }

        public Criteria and{{firstUpFieldName}}NotLike({{javaType}} {{fieldName}}) {
            addCriterion("{{columnName}} not like", {{fieldName}}, "{{fieldName}}");
            return (Criteria) this;
        }
        {{/stringType}}
{{/metadata.columnMetadataList}}

    }

    public static class Criteria extends GeneratedCriteria implements Serializable {

        private static final long serialVersionUID = 9185867838086944489L;


        protected Criteria() {
            super();
        }
    }

    public static class Criterion implements Serializable {

        private static final long serialVersionUID = 5502500909305190410L;

        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }

    protected static List<String> allColumns(){
        List<String> columns = new ArrayList<String>();
        {{#metadata.columnMetadataList}}
        columns.add("{{columnName}}");
        {{/metadata.columnMetadataList}}
        return columns;
    }

    {{#metadata.partitionKey}}
    protected static List<String> getShardTables(){
        List<String> tables = new ArrayList<String>();
        {{#metadata.shardTables}}
        tables.add("{{.}}");
        {{/metadata.shardTables}}
        return tables;
    }
    {{/metadata.partitionKey}}

}